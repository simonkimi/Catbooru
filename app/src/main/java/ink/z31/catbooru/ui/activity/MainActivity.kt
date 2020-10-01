package ink.z31.catbooru.ui.activity


import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import co.zsmb.materialdrawerkt.builders.accountHeader
import co.zsmb.materialdrawerkt.builders.drawer
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.google.android.material.snackbar.Snackbar
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import ink.z31.catbooru.R
import ink.z31.catbooru.data.database.AppDatabase
import ink.z31.catbooru.data.database.dao.BooruDao
import ink.z31.catbooru.ui.adapter.PreviewAdapter
import ink.z31.catbooru.ui.viewModel.MainViewModel
import ink.z31.catbooru.ui.widget.recyclerView.SearchBarMover
import ink.z31.catbooru.ui.adapter.SearchBarSuggestionsAdapter
import ink.z31.catbooru.ui.widget.searchview.SearchView
import ink.z31.catbooru.util.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.searchView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*


class MainActivity : AppCompatActivity(), SearchBarMover.Helper {
    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var materialDrawer: Drawer
    private lateinit var headerResult: AccountHeader
    private lateinit var viewModel: MainViewModel
    private lateinit var booruListDao: BooruDao


    private lateinit var previewAdapter: PreviewAdapter

    private val onSuccess = {
        this@MainActivity.progressBar.visibility = View.INVISIBLE
        previewAdapter.loadMoreModule.loadMoreComplete()
    }
    private val onEnd = {
        this@MainActivity.progressBar.visibility = View.INVISIBLE
        previewAdapter.loadMoreModule.loadMoreEnd()
    }
    private val onFail = { it: String ->
        this@MainActivity.progressBar.visibility = View.INVISIBLE
        previewAdapter.loadMoreModule.loadMoreComplete()
        this.addRetryFooter(it)
    }

    private var profileSettingItem = ProfileSettingDrawerItem()
        .withName(R.string.booruManager)
        .withIcon(GoogleMaterial.Icon.gmd_settings)
        .withIdentifier(999_999_999)
        .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
            override fun onItemClick(
                view: View?,
                position: Int,
                drawerItem: IDrawerItem<*>
            ): Boolean {
                val intent = Intent(this@MainActivity, BooruManagerActivity::class.java)
                startActivity(intent)
                return false
            }
        })

    private val loadMoreListener = OnLoadMoreListener {
        Log.i(TAG, "下拉加载下一面")
        viewModel.launchNextPage(
            onSuccess = {
                onSuccess()
                previewAdapter.loadMoreModule.loadMoreComplete()
            },
            onEnd = onEnd,
            onFail = onFail
        )
    }
    private var lastPressBack = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        booruListDao = AppDatabase.getDatabase(this).booruDao()
        EventBus.getDefault().register(this)
        // 侧滑tag
        val toolbar = this.tagToolBar
        toolbar.title = this.getString(R.string.quickSearch)
        viewModel.initBooruAsync {
            initSearchBar()
            initPreview()
            initSideBar(savedInstanceState)
            previewAdapter.loadMoreModule.loadMoreToLoading()
        }
    }

    @Suppress("UNUSED")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onBooruChanged(msg: EventMsg) {
        if (msg.type == EventType.BOORU_CHANGE) {
            viewModel.getAllBooruAsync()
        }
    }

    private fun addRetryFooter(errMsg: String) {
        val view: View =
            layoutInflater.inflate(R.layout.item_preview_retry, this.previewRecyclerView, false)
        view.findViewById<TextView>(R.id.textViewErrMsg).text = errMsg
        view.findViewById<Button>(R.id.buttonRetry).setOnClickListener {
            if (viewModel.booruPostList.value?.size ?: 0 == 0) {
                this.progressBar.visibility = View.VISIBLE
            }
            previewAdapter.footerLayout?.removeAllViews()
            previewAdapter.notifyDataSetChanged()

            previewAdapter.loadMoreModule.setOnLoadMoreListener(loadMoreListener)
            viewModel.launchNextPage(
                onSuccess = {
                    onSuccess()
                    previewAdapter.loadMoreModule.loadMoreComplete()
                },
                onEnd = onEnd,
                onFail = onFail
            )
        }
        previewAdapter.loadMoreModule.setOnLoadMoreListener(null)
        previewAdapter.addFooterView(view, 0)
    }

    private fun initSearchBar() {
        // 初始化搜索条
        val suggestionAdapter = viewModel.tagSuggestionAdapter
        val searchViewHelper = object : SearchView.Helper {
            override fun onSearchEditTextClick() {

            }

            override fun onSearchEditTextBackPressed() {
                if (searchView.state == SearchView.Companion.STATE.STATE_SEARCH) {
                    searchView.setSearchState(SearchView.Companion.STATE.STATE_MAIN)
                }
            }

            override fun onLeftButtonClick() {

                when(searchView.state) {
                    SearchView.Companion.STATE.STATE_SEARCH -> {
                        searchView.setSearchState(SearchView.Companion.STATE.STATE_MAIN)
                    }
                    SearchView.Companion.STATE.STATE_MAIN -> {
                        materialDrawer.openDrawer()
                    }
                }

            }

            override fun onRightButtonClick() {
                when(searchView.state) {
                    SearchView.Companion.STATE.STATE_SEARCH -> {
                        searchView.text = ""
                    }
                    SearchView.Companion.STATE.STATE_MAIN -> {
                        // TODO 设计过滤器
                    }
                }
            }

            override fun onSearch(key: String) {
                this@MainActivity.progressBar.visibility = View.VISIBLE
                viewModel.launchNewSearchAsync(
                    tags = key,
                    onSuccess = onSuccess,
                    onEnd = onEnd,
                    onFail = onFail
                )
                searchView.titleHint = key
                searchView.setSearchState(SearchView.Companion.STATE.STATE_MAIN)
            }

            override fun onHintTextClick() {
                searchView.setSearchState(SearchView.Companion.STATE.STATE_SEARCH)
                val imm: InputMethodManager =
                    AppUtil.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
            }

            override fun onSearchTextChange(text: String) {
                suggestionAdapter.filter.filter(text)
            }

        }
        suggestionAdapter.suggestionHelper = object :
            SearchBarSuggestionsAdapter.SuggestionHelper {
            override fun onSuggestionClick(suggestion: String, position: Int) {
                if (searchView.text.trim().isEmpty()) {
                    searchView.text = "$suggestion "
                } else {
                    val tags = searchView.text.split(" ")
                    val tagsLast = tags.subList(0, tags.lastIndex).toMutableList()
                    tagsLast.add(suggestion)
                    searchView.text = "${tagsLast.joinToString(" ")} "
                }
                suggestionAdapter.filter.filter(searchView.text)
            }
        }

        searchView.helper = searchViewHelper
        searchView.suggestionsAdapter = suggestionAdapter


        SearchBarMover(
            this,
            this.searchView,
            this.previewRecyclerView
        )
    }

    private fun initSideBar(savedInstanceState: Bundle?) {
        // 侧滑菜单
        materialDrawer = drawer {
            hasStableIds = true
            savedInstance = savedInstanceState

            // 用户一栏
            headerResult = accountHeader {
                savedInstance = savedInstanceState
                translucentStatusBar = true

                onProfileChanged { _, profile, _ ->
                    if (profile.identifier != 999_999_999.toLong()) {
                        viewModel.launchNewBooruAsync(
                            booruId = profile.identifier.toInt(),
                            onSuccess = {
                                previewAdapter.loadMoreModule.loadMoreToLoading()
                            }
                        )
                        SPUtil.set("main") {
                            putLong("start_booru_id", profile.identifier)
                        }
                    }
                    false
                }
            }
        }
        this.viewModel.booruList.observe(this) { list ->
            headerResult.clear()
            var activityProfile: ProfileDrawerItem? = null
            list.map {
                val p = ProfileDrawerItem()
                    .withName(it.title)
                    .withNameShown(true)
                    .withEmail(it.host)
                    .withIdentifier(it.id)
                    .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                        override fun onItemClick(
                            view: View?,
                            position: Int,
                            drawerItem: IDrawerItem<*>
                        ): Boolean {
                            viewModel.launchNewBooruAsync(
                                booruId = it.id.toInt(),
                                onSuccess = {
                                    previewAdapter.loadMoreModule.loadMoreToLoading()
                                },
                            )
                            SPUtil.set("main") {
                                putLong("start_booru_id", it.id)
                            }
                            return false
                        }
                    })
                if (viewModel.booru == it) {
                    activityProfile = p
                }
                if (it.favicon.isNotEmpty()) {
                    val bytes = Base64Util.b64Decode(it.favicon.toByteArray())
                    val icon = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    val matrix = Matrix()
                    matrix.setScale(5F, 5F)
                    val newIcon =
                        Bitmap.createBitmap(icon, 0, 0, icon.width, icon.height, matrix, true)
                    p.withIcon(newIcon)
                }
                p
            }.forEach {
                headerResult.addProfile(it, headerResult.profiles?.size ?: 0)
            }
            activityProfile.let {
                headerResult.activeProfile = it
            }
            headerResult.addProfile(profileSettingItem, headerResult.profiles?.size ?: 0)
        }
        EventBus.getDefault().post(EventMsg(EventType.BOORU_CHANGE))
    }

    /**
     * 初始化预览界面
     */
    private fun initPreview() {
        this.previewRecyclerView.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        previewAdapter = PreviewAdapter(mutableListOf())
        // 上拉加载
        previewAdapter.loadMoreModule.setOnLoadMoreListener(loadMoreListener)
        // 预览图
        this.viewModel.booruPostList.observe(this) { booruPost ->
            booruPost?.let {
                previewAdapter.setData(it)
                previewAdapter.notifyDataSetChanged()
            }
        }
        //  详情界面
        previewAdapter.setOnItemClickListener { _, view, position ->
            val booruPost = previewAdapter.data[position]
            val intent = Intent(this, PostActivity::class.java)
            val transition = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                view.findViewById(R.id.previewImg),
                "postImg"
            )
            intent.putExtra("booruPost", booruPost)
            viewModel.updateTagStore(booruPost.tags)
            startActivity(intent, transition.toBundle())
        }
        this.previewRecyclerView.adapter = previewAdapter
    }


    override fun isValidView(recyclerView: RecyclerView?): Boolean {
        return recyclerView == this.previewRecyclerView
    }

    override fun getValidRecyclerView(): RecyclerView? {
        return this.previewRecyclerView
    }

    override fun forceShowSearchBar(): Boolean {
        return false
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (searchView.suggestionOpen) {
            searchView.suggestionOpen = false
        } else if (materialDrawer.isDrawerOpen) {
            materialDrawer.closeDrawer()
        } else {
            val now = Date().time
            if (now - lastPressBack > 1000) {
                lastPressBack = now
                Snackbar.make(this.main_root, R.string.pressBackAgain, Snackbar.LENGTH_SHORT).show()
            } else {
                super.onBackPressed()
            }
        }
    }
}

