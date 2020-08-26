package ink.z31.catbooru.ui.activity

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.DropDownPreference
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import ink.z31.catbooru.R
import ink.z31.catbooru.util.ISettingFragment
import ink.z31.catbooru.util.SettingBaseFragment
import ink.z31.catbooru.util.SettingPreferenceFragment
import kotlinx.android.synthetic.main.settings_activity.*


enum class SettingActivityTarget(val value: Int) {
    ADD_BOORU(0)
}


class SettingActivity : AppCompatActivity() {
    lateinit var fragment: ISettingFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        fragment = when (intent.getIntExtra("target", 0)) {
            SettingActivityTarget.ADD_BOORU.value -> {
                this.booruSettingToolbar.setTitle(R.string.addBooru)
                AddBooruFragment(this.booruSettingToolbar)
            }
            else -> object : SettingBaseFragment() {
                override fun getMenuRes(): Int? {
                    return null
                }
            }
        }

        setSupportActionBar(this.booruSettingToolbar)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, fragment as AddBooruFragment)
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

class AddBooruFragment(private val toolbar: Toolbar) : SettingPreferenceFragment() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.add_booru_preference, rootKey)
        val booruName = findPreference<EditTextPreference>("booru_name")
        val booruHost = findPreference<EditTextPreference>("booru_host")
        val booruType = findPreference<DropDownPreference>("booru_type")
        val onEditPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { preference, newValue ->
                preference.summary = newValue as String
                preference.setDefaultValue(newValue)
                true
            }
        booruName?.onPreferenceChangeListener = onEditPreferenceChangeListener
        booruHost?.onPreferenceChangeListener = onEditPreferenceChangeListener
        booruType?.setOnPreferenceChangeListener { preference, newValue ->
            val index = (newValue as String).toInt()
            preference.summary = resources.getStringArray(R.array.booruType)[index]
            preference.setDefaultValue(newValue)
            true
        }
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.booru_save -> {
                    Toast.makeText(context, "Booru Save", Toast.LENGTH_LONG).show()
                }
            }
            true
        }
    }

    override fun getMenuRes(): Int? {
        return R.menu.add_booru
    }
}