package ink.z31.catbooru.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ink.z31.catbooru.R
import ink.z31.catbooru.ui.adapter.BooruItemAdapter
import ink.z31.catbooru.ui.viewModel.MainViewModel
import ink.z31.catbooru.ui.viewModel.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_booru_manager.*

class BooruManagerActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booru_manager)
        // 设置标题栏
        setSupportActionBar(this.booruManagerToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.booruManagerToolbar.setOnMenuItemClickListener { item ->
            when(item.itemId) {
                R.id.manager_add -> {
                    val intent = Intent(this, SettingActivity::class.java)
                    intent.putExtra("target", SettingActivityTarget.ADD_BOORU.value)
                    startActivity(intent)
                }
            }
            true
        }
        // 加载数据
        mainViewModel = ViewModelProvider(this, MainViewModelFactory(application)).get(MainViewModel::class.java)
        val layoutManager = LinearLayoutManager(this)
        val adapter = BooruItemAdapter(mainViewModel.booruList.value ?: mutableListOf())
        mainViewModel.booruList.observe(this, Observer { booruList ->
            booruList?.let {
                adapter.setData(booruList)
                adapter.notifyDataSetChanged()
            }
        })
        booru_item.layoutManager = layoutManager
        booru_item.adapter = adapter
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.booru_manager_menu, menu)
        return true
    }

}