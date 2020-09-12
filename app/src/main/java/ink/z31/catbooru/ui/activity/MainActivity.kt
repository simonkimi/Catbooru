package ink.z31.catbooru.ui.activity


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import co.zsmb.materialdrawerkt.builders.accountHeader
import co.zsmb.materialdrawerkt.builders.drawer
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
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
import ink.z31.catbooru.data.model.base.BooruPost
import ink.z31.catbooru.ui.adapter.PreviewViewModel
import ink.z31.catbooru.ui.viewModel.MainViewModel
import ink.z31.catbooru.ui.widget.recyclerView.SearchBarMover
import ink.z31.catbooru.util.*
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), SearchBarMover.Helper {

    private lateinit var materialDrawer: Drawer
    private lateinit var headerResult: AccountHeader
    private lateinit var viewModel: MainViewModel
    private lateinit var booruListDao: BooruDao
    private lateinit var profileSettingItem: ProfileSettingDrawerItem
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
        profileSettingItem = ProfileSettingDrawerItem()
            .withName(R.string.booruManager)
            .withIcon(GoogleMaterial.Icon.gmd_settings)
            .withIdentifier(100_001)
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


        // 侧滑菜单
        materialDrawer = drawer {
            hasStableIds = true
            savedInstance = savedInstanceState

            // 用户一栏
            headerResult = accountHeader {
                savedInstance = savedInstanceState
                translucentStatusBar = true
            }
        }

        initSearchBar()
        initPreview()
        initSideBar()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onBooruChanged(msg: EventMsg) {
        if (msg.type == EventType.BOORU_CHANGE) {
            viewModel.getAllBooruAsync()
        }
    }

    private fun initSearchBar() {
        // 初始化搜索条
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
                viewModel.launchNewSearchAsync(text.toString())
            }
        })
        this.searchBar.lastSuggestions
        SearchBarMover(
            this,
            this.searchBar,
            this.previewRecyclerView
        )
        this.searchBar.elevation = 5F
    }

    private fun initSideBar() {
        this.viewModel.booruList.observe(this, Observer { list ->
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
                            viewModel.launchNewBooruAsync(it.id.toInt())
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
        })
        EventBus.getDefault().post(EventMsg(EventType.BOORU_CHANGE))
    }

    /**
     * 初始化预览界面
     */
    private fun initPreview() {
        val layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        this.previewRecyclerView.layoutManager = layoutManager
        val adapter = PreviewViewModel(mutableListOf())
        // 上拉加载
        adapter.loadMoreModule.setOnLoadMoreListener {
            Log.i(TAG, "加载下一面")
            viewModel.launchNextPage()
            adapter.loadMoreModule.loadMoreComplete()
        }
        // 预览图
        this.viewModel.booruPostList.observe(this, Observer { booruPost ->
            booruPost?.let {
                adapter.setData(booruPost)
                adapter.notifyDataSetChanged()
            }
        })
        // 是否最后一面
        this.viewModel.booruPostEnd.observe(this, Observer {
            if (it) {
                adapter.loadMoreModule.loadMoreEnd()
            } else {
                adapter.setNewInstance(adapter.data)
            }
        })
        // 加载进度条
        this.viewModel.progressBarVis.observe(this, Observer {
            progressBar.visibility = if (it) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
        })
        //  详情界面
        adapter.setOnItemClickListener { _, _, position ->
            val booruPost = adapter.data[position]
            val booruJson = Gson().toJson(booruPost)
            val intent = Intent(this, PostActivity::class.java)
            intent.putExtra("booruJson", booruJson)
            startActivity(intent)
        }
        this.previewRecyclerView.adapter = adapter
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
        val now = Date().time
        if (now - lastPressBack > 1000) {
            lastPressBack = now
            Snackbar.make(this.main_root, R.string.pressBackAgain, Snackbar.LENGTH_SHORT).show()
        } else {
            super.onBackPressed()
        }
    }
}

