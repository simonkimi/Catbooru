package ink.z31.catbooru.ui.fragment

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.preference.DropDownPreference
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import ink.z31.catbooru.R
import ink.z31.catbooru.data.database.AppDatabase
import ink.z31.catbooru.data.database.Booru
import ink.z31.catbooru.ui.activity.ISettingFragment
import ink.z31.catbooru.ui.viewModel.BooruViewModel
import ink.z31.catbooru.ui.viewModel.MainViewModel
import ink.z31.catbooru.util.AppUtil
import ink.z31.catbooru.util.EventMsg
import ink.z31.catbooru.util.EventType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus

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
                    GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            val dao = AppDatabase.getDatabase(requireContext()).booruDao()
                            val booru = Booru(
                                title = booruName!!.text,
                                type = booruType!!.value.toInt(),
                                host = booruHost!!.text
                            )
                            dao.insertBooru(booru)
                        }
                        EventBus.getDefault().post(EventMsg(EventType.BOORU_CHANGE))
                    }
                    Toast.makeText(AppUtil.context, getText(R.string.saveSucceeded), Toast.LENGTH_LONG).show()
                    activity?.finish()
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