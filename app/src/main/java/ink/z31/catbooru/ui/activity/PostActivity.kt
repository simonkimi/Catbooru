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
import ink.z31.catbooru.data.database.AppDatabase
import ink.z31.catbooru.data.database.TagStore
import ink.z31.catbooru.data.model.base.BooruPost
import ink.z31.catbooru.ui.fragment.PostDetailFragment
import ink.z31.catbooru.ui.viewModel.PostViewModel
import ink.z31.catbooru.util.AppUtil
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        // 初始化数据
        val booruPost = intent.getParcelableExtra<BooruPost>("booruPost")!!
        ViewModelProvider(
            this,
            PostViewModel.PostViewModelFactory(booruPost)
        ).get(PostViewModel::class.java)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            supportFinishAfterTransition()
        }
    }
}