package ink.z31.catbooru.ui.activity

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ink.z31.catbooru.R
import ink.z31.catbooru.ui.fragment.AddBooruFragment
import kotlinx.android.synthetic.main.settings_activity.*


class SettingActivity : AppCompatActivity() {

    enum class Target(val value: Int) {
        ADD_BOORU(0)
    }

    lateinit var fragment: ISettingFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        fragment = when (intent.getIntExtra("target", 0)) {
            Target.ADD_BOORU.value -> {
                this.booruSettingToolbar.setTitle(R.string.addBooru)
                AddBooruFragment(this.booruSettingToolbar)
            }
            else -> object : Fragment(), ISettingFragment {
                override fun getMenuRes(): Int? {
                    return null
                }
            }
        }

        setSupportActionBar(this.booruSettingToolbar)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, fragment as Fragment)
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val res = fragment.getMenuRes()
        res?.let {
            menuInflater.inflate(it, menu)
            return true
        }
        return false
    }

}


interface ISettingFragment {
    fun getMenuRes(): Int?
}