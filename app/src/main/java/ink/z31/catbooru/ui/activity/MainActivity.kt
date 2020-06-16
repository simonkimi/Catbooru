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

            }
        })
        initPreview()
    }

    private fun initPreview() {
        val layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        this.previewRecyclerView.layoutManager = layoutManager
        val adapter = TagAdapter(mutableListOf())
        this.previewRecyclerView.adapter = adapter
        this.viewModel.preViewImgList.observe(this, Observer { preViewImgList ->
            adapter.setData(preViewImgList)
            adapter.notifyDataSetChanged()
        })
    }
}

