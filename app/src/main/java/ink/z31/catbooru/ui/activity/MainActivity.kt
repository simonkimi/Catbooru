package ink.z31.catbooru.ui.activity


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
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
import com.mancj.materialsearchbar.MaterialSearchBar
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
import ink.z31.catbooru.ui.widget.searchBar.SearchBarSuggestionsAdapter
import ink.z31.catbooru.ui.widget.searchBar.SearchSuggestion
import ink.z31.catbooru.util.Base64Util
import ink.z31.catbooru.util.EventMsg
import ink.z31.catbooru.util.EventType
import ink.z31.catbooru.util.SPUtil
import kotlinx.android.synthetic.main.activity_main.*
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
        val adaptor = viewModel.tagSuggestionAdapter
        adaptor.setOnSuggestionClickListener(object :
            SearchBarSuggestionsAdapter.OnSuggestionClickListener {
            override fun onClick(suggestion: SearchSuggestion, position: Int) {
                if (searchBar.text.trim().isEmpty()) {
                    searchBar.text = "${suggestion.suggestion} "
                } else {
                    val tags = searchBar.text.split(" ")
                    val tagsLast = tags.subList(0, tags.lastIndex).toMutableList()
                    tagsLast.add(suggestion.suggestion)
                    searchBar.text = "${tagsLast.joinToString(" ")} "
                }
                searchBar.searchEditText.setSelection(searchBar.text.length)
            }
        })
        this.searchBar.setCustomSuggestionAdapter(adaptor)
        this.searchBar.setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener {
            override fun onButtonClicked(buttonCode: Int) {
                when (buttonCode) {
                    MaterialSearchBar.BUTTON_NAVIGATION -> {
                        materialDrawer.openDrawer()
                    }
                }
            }

            override fun onSearchStateChanged(enabled: Boolean) {

            }

            override fun onSearchConfirmed(text: CharSequence?) {
                this@MainActivity.progressBar.visibility = View.VISIBLE
                viewModel.launchNewSearchAsync(
                    tags = text.toString(),
                    onSuccess = onSuccess,
                    onEnd = onEnd,
                    onFail = onFail
                )
                this@MainActivity.searchBar.hideSuggestionsList()
            }
        })

        this.searchBar.addTextChangeListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                adaptor.filter.filter(this@MainActivity.searchBar.text)
                adaptor.notifyDataSetChanged()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        this.searchBar.searchEditText.setOnFocusChangeListener { _, has ->
            if (has) {
                this.searchBar.showSuggestionsList()
            } else {
                this.searchBar.hideSuggestionsList()
            }
        }

        SearchBarMover(
            this,
            this.searchBarContainer,
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
        if (this.searchBar.isSuggestionsVisible) {
            this.searchBar.hideSuggestionsList()
        }
        else if (materialDrawer.isDrawerOpen) {
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

