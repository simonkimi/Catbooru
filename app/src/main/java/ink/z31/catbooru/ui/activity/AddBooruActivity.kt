package ink.z31.catbooru.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.DropDownPreference
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import ink.z31.catbooru.R
import kotlinx.android.synthetic.main.settings_activity.*

class AddBooruActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, AddBooruFragment())
            .commit()
        setSupportActionBar(this.booruSettingToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class AddBooruFragment : PreferenceFragmentCompat() {
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
    }
}