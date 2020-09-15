package ink.z31.catbooru.ui.activity

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.google.gson.Gson
import ink.z31.catbooru.R
import ink.z31.catbooru.data.model.base.BooruPost
import ink.z31.catbooru.ui.fragment.PostDetailFragment
import ink.z31.catbooru.ui.fragment.PostPreviewFragment
import ink.z31.catbooru.ui.viewModel.PostViewModel
import kotlinx.android.synthetic.main.activity_post.*


class PostActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "PostActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
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