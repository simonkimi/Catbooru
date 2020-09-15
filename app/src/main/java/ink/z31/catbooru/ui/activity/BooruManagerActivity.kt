package ink.z31.catbooru.ui.activity

import android.content.Intent
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.listener.OnItemSwipeListener
import ink.z31.catbooru.R
import ink.z31.catbooru.ui.adapter.BooruItemAdapter
import ink.z31.catbooru.ui.viewModel.BooruViewModel
import ink.z31.catbooru.util.EventMsg
import ink.z31.catbooru.util.EventType
import kotlinx.android.synthetic.main.activity_booru_manager.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class BooruManagerActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "BooruManagerActivity"
    }
    private lateinit var booruViewModel: BooruViewModel

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onBooruChanged(msg: EventMsg) {
        if (msg.type == EventType.BOORU_CHANGE) {
            booruViewModel.getAllBooruAsync()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booru_manager)
        EventBus.getDefault().register(this)
        // 设置标题栏
        this.booruManagerToolbar.setTitle(R.string.booruManager)
        setSupportActionBar(this.booruManagerToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.booruManagerToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.manager_add -> {
                    val intent = Intent(this, SettingActivity::class.java)
                    intent.putExtra("target", SettingActivity.Target.ADD_BOORU.value)
                    startActivity(intent)
                }
            }
            true
        }
        this.booruManagerToolbar.setNavigationOnClickListener {
            finish()
        }
        // 加载数据
        booruViewModel = ViewModelProvider(this).get(BooruViewModel::class.java)
        val layoutManager = LinearLayoutManager(this)
        val adapter = BooruItemAdapter(booruViewModel.booruList.value ?: mutableListOf())
        adapter.draggableModule.isSwipeEnabled = true
        adapter.draggableModule.setOnItemSwipeListener(object : OnItemSwipeListener {
            override fun onItemSwipeStart(viewHolder: RecyclerView.ViewHolder?, pos: Int) {

            }

            override fun clearView(viewHolder: RecyclerView.ViewHolder?, pos: Int) {

            }

            override fun onItemSwiped(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
                AlertDialog.Builder(this@BooruManagerActivity)
                    .setTitle("Catbooru")
                    .setMessage("Delete?")
                    .setPositiveButton("Yes") { _, _ ->
                        val booru = booruViewModel.booruList.value!![pos]
                        booruViewModel.deleteBooruAsync(booru)
                    }
                    .setNegativeButton("No") { _, _ ->
                        booruViewModel.getAllBooruAsync()
                    }
                    .create()
                    .show()
            }

            override fun onItemSwipeMoving(
                canvas: Canvas?,
                viewHolder: RecyclerView.ViewHolder?,
                dX: Float,
                dY: Float,
                isCurrentlyActive: Boolean
            ) {

            }

        })
        booruViewModel.booruList.observe(this, Observer { booruList ->
            Log.i(TAG, "notifyDataSetChangedAAA")
            booruList?.let {
                adapter.setData(it)
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