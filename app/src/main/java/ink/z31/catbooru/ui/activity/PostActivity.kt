package ink.z31.catbooru.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import ink.z31.catbooru.R
import ink.z31.catbooru.data.model.base.BooruPost
import ink.z31.catbooru.ui.fragment.PostDetailFragment
import ink.z31.catbooru.ui.fragment.PostPreviewFragment
import ink.z31.catbooru.ui.interfaces.IOpenPostDetail
import ink.z31.catbooru.ui.viewModel.PostViewModel
import kotlinx.android.synthetic.main.activity_post.*


class PostActivity : IOpenPostDetail, AppCompatActivity() {
    companion object {
        private const val TAG = "PostActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        // 初始化数据
        val booruJson: String = intent.getStringExtra("booruJson")!!
        val booruPost = Gson().fromJson(booruJson, BooruPost::class.java)
        val viewModel = ViewModelProvider(this).get(PostViewModel::class.java)
        viewModel.init(booruPost)
        // 初始化Toolbar
        this.postToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.download -> {
                    Toast.makeText(this, "点击下载按钮", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
        this.postToolbar.title = ""
        this.postToolbar.fitsSystemWindows = true
        setSupportActionBar(this.postToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.post_menu, menu)
        return true
    }

    override fun openPostDetail() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.postFragment, PostDetailFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()
                } else {
                    finish()
                }
            }
        }
        return true
    }
}