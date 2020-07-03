package ink.z31.catbooru.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import ink.z31.catbooru.R
import kotlinx.android.synthetic.main.activity_booru_manager.*
import kotlinx.android.synthetic.main.activity_post.*

class BooruManagerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booru_manager)
        setSupportActionBar(this.booruManagerToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.booru_manager_menu, menu)
        return true
    }

}