package ink.z31.catbooru.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import ink.z31.catbooru.R
import ink.z31.catbooru.data.database.AppDatabase
import ink.z31.catbooru.ui.adapter.BooruItemAdapter
import kotlinx.android.synthetic.main.activity_booru_manager.*

class BooruManagerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booru_manager)
        // 设置标题栏
        setSupportActionBar(this.booruManagerToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.booruManagerToolbar.setOnMenuItemClickListener { item ->
            when(item.itemId) {
                R.id.manager_add -> {
                    val intent = Intent(this, AddBooruActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }
        // 加载数据
        val booruDao = AppDatabase.getDatabase(this).booruDao()
        val adapter = BooruItemAdapter(booruDao.getAllBooru().toMutableList())

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.booru_manager_menu, menu)
        return true
    }

}