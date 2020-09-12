package ink.z31.catbooru.ui.activity

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.google.gson.Gson
import ink.z31.catbooru.R
import ink.z31.catbooru.data.model.base.BooruPost
import kotlinx.android.synthetic.main.activity_post.*


class PostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        this.postToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.showInfo -> {
                    Toast.makeText(this, "点击信息按钮", Toast.LENGTH_SHORT).show()
                }
                R.id.download -> {
                    Toast.makeText(this, "点击下载按钮", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
        val booruJson: String = intent.getStringExtra("booruJson")!!
        val booru = Gson().fromJson(booruJson, BooruPost::class.java)
        this.postToolbar.title = "Post ${booru.id}"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setSupportActionBar(this.postToolbar)
        initPost(booru)

    }

    private fun initPost(booru: BooruPost) {
        Glide.with(this)
            .load(booru.imgURL)
            .thumbnail(
                Glide.with(this).load(R.mipmap.loading)
            )
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(object : CustomViewTarget<SubsamplingScaleImageView, Drawable>(imageView) {
                override fun onLoadFailed(errorDrawable: Drawable?) {
                }

                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    val bitmap: Bitmap = resource.toBitmap()
                    imageView.setImage(ImageSource.bitmap(bitmap))
                }

                override fun onResourceCleared(placeholder: Drawable?) {

                }

            })
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.post_menu, menu)
        return true
    }


}