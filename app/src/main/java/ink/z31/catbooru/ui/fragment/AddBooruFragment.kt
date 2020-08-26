package ink.z31.catbooru.ui.fragment

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.preference.DropDownPreference
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import ink.z31.catbooru.R
import ink.z31.catbooru.ui.activity.ISettingFragment

class AddBooruFragment(private val toolbar: Toolbar) :
    PreferenceFragmentCompat(),
    ISettingFragment {

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
        toolbar.setNavigationOnClickListener {
            activity?.finish()
        }
    }

    override fun getMenuRes(): Int? {
        return R.menu.add_booru
    }
}