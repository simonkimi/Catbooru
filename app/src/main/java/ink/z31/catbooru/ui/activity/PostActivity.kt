package ink.z31.catbooru.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.marginTop
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import ink.z31.catbooru.R
import ink.z31.catbooru.data.model.base.BooruPost
import ink.z31.catbooru.ui.fragment.PostPreviewFragment
import ink.z31.catbooru.ui.viewModel.PostViewModel
import ink.z31.catbooru.util.ViewUtils
import kotlinx.android.synthetic.main.activity_post.*


class PostActivity : AppCompatActivity() {
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
        // 初始化Fragment
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.postFragment, PostPreviewFragment())
            .commit()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.post_menu, menu)
        return true
    }
}