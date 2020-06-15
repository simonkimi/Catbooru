package ink.z31.catbooru.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import co.zsmb.materialdrawerkt.builders.accountHeader
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.profile.profile
import co.zsmb.materialdrawerkt.draweritems.profile.profileSetting
import com.mancj.materialsearchbar.MaterialSearchBar
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.Drawer
import ink.z31.catbooru.R
import ink.z31.catbooru.data.model.base.BooruPreviewImage
import ink.z31.catbooru.ui.adapter.TagAdapter
import ink.z31.catbooru.ui.viewModel.MainModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var materialDrawer: Drawer
    private lateinit var headerResult: AccountHeader
    private lateinit var viewModel: MainModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainModel::class.java)
        // 侧滑tag
        val toolbar = this.tagToolBar
        toolbar.title = "TAG"
        // 侧滑菜单
        materialDrawer = drawer {
            hasStableIds = true
            savedInstance = savedInstanceState

            // 用户一栏
            headerResult = accountHeader {
                savedInstance = savedInstanceState
                translucentStatusBar = true
                profileSetting(this@MainActivity.getString(R.string.head_profile_manage)) {
                    iicon = GoogleMaterial.Icon.gmd_settings
                    identifier = 100_001
                }
            }
        }

        // 初始化搜索条
        this.searchBar.setOnSearchActionListener(object: MaterialSearchBar.OnSearchActionListener {
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

            }
        })
        initPreview()
    }

    private fun initPreview() {
        val layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        this.previewRecyclerView.layoutManager = layoutManager
        val adapter = TagAdapter(mutableListOf(
            BooruPreviewImage(5359072, "https://img1.gelbooru.com/thumbnails/84/c9/thumbnail_84c93527ffaa1320998cdf6c19500a2c.jpg", listOf()),
            BooruPreviewImage(5359069, "https://img1.gelbooru.com/thumbnails/65/0a/thumbnail_650a0780382027de6771c94b9f9ac787.jpg", listOf()),
            BooruPreviewImage(5359017, "https://img1.gelbooru.com/thumbnails/5c/f9/thumbnail_5cf9ebcac2185d149c1dd0dd57e7c674.jpg", listOf()),
            BooruPreviewImage(5358984, "https://img1.gelbooru.com/thumbnails/6b/58/thumbnail_6b583c146a3bebb1762435e5915271a1.jpg", listOf()),
            BooruPreviewImage(5358906, "https://img1.gelbooru.com/thumbnails/22/74/thumbnail_2274612b6befae9be074bf668520efc3.jpg", listOf()),
            BooruPreviewImage(5358905, "https://img1.gelbooru.com/thumbnails/36/f9/thumbnail_36f9381c1f04800a36016bde0bba72a8.jpg", listOf()),
            BooruPreviewImage(5358896, "https://img1.gelbooru.com/thumbnails/c3/27/thumbnail_c327e3969f763f50ac2552c69f63241c.jpg", listOf()),
            BooruPreviewImage(5358856, "https://img1.gelbooru.com/thumbnails/99/70/thumbnail_9970e21d513d94e82e56bbd58111c318.jpg", listOf()),
            BooruPreviewImage(5358641, "https://img1.gelbooru.com/thumbnails/69/c8/thumbnail_69c856403184c429344a22e856eee0c3.jpg", listOf()),
            BooruPreviewImage(5358637, "https://img1.gelbooru.com/thumbnails/d6/32/thumbnail_d632d59897f6d4d23be31bde206f5c01.jpg", listOf()),
            BooruPreviewImage(5358557, "https://img1.gelbooru.com/thumbnails/ed/38/thumbnail_ed38731db5554420833d1139703ac5aa.jpg", listOf()),
            BooruPreviewImage(5358532, "https://img1.gelbooru.com/thumbnails/0d/2a/thumbnail_0d2a05b64f73323be8047241f8007f04.jpg", listOf()),
            BooruPreviewImage(5358492, "https://img1.gelbooru.com/thumbnails/0d/41/thumbnail_0d41c13a1131cbe66bcb3909d3a6a02f.jpg", listOf()),
            BooruPreviewImage(5358343, "https://img1.gelbooru.com/thumbnails/72/83/thumbnail_728325b3f02595457a9c494a060dae61.jpg", listOf()),
            BooruPreviewImage(5358287, "https://img1.gelbooru.com/thumbnails/9a/3c/thumbnail_9a3c47fc169174cec0b7d9d30e806cd0.jpg", listOf()),
            BooruPreviewImage(5358266, "https://img1.gelbooru.com/thumbnails/e8/33/thumbnail_e833036711f3cba7dcf17f41401f67e4.jpg", listOf()),
            BooruPreviewImage(5358265, "https://img1.gelbooru.com/thumbnails/1c/be/thumbnail_1cbed4a6ac1e9ebe01c3551a420641e0.jpg", listOf()),
            BooruPreviewImage(5358213, "https://img1.gelbooru.com/thumbnails/fd/fd/thumbnail_fdfd90fef620327dec90a3fee3c6546f.jpg", listOf()),
            BooruPreviewImage(5358209, "https://img1.gelbooru.com/thumbnails/75/ed/thumbnail_75edd38eb63ec1e411865aea58df3786.jpg", listOf())
        ))
        this.previewRecyclerView.adapter = adapter
    }
}

