package ink.z31.catbooru.ui.activity

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.preference.DropDownPreference
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import ink.z31.catbooru.R
import ink.z31.catbooru.util.ISettingFragment
import ink.z31.catbooru.util.SettingPreferenceFragment
import kotlinx.android.synthetic.main.settings_activity.*

class AddBooruActivity : AppCompatActivity() {
    lateinit var fragment: ISettingFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        fragment = AddBooruFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, fragment as AddBooruFragment)
            .commit()
        setSupportActionBar(this.booruSettingToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(fragment.getMenu(), menu)
        return true
    }

    class AddBooruFragment : SettingPreferenceFragment() {
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
        }

        override fun getMenu(): Int {
            return R.menu.add_booru
        }
    }
}