package ink.z31.catbooru.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.davemorrissey.labs.subscaleview.ImageSource
import ink.z31.catbooru.R
import kotlinx.android.synthetic.main.activity_post.*

class PostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        setSupportActionBar(this.postToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        test()
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
    }

    private fun test() {
        this.postToolbar.title = "POST 123456"
        this.imageView.setImage(ImageSource.resource(R.mipmap.test))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.post_menu, menu)
        return true
    }

}