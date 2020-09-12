package ink.z31.catbooru.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.preference.DropDownPreference
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import ink.z31.catbooru.R
import ink.z31.catbooru.data.database.AppDatabase
import ink.z31.catbooru.data.database.Booru
import ink.z31.catbooru.ui.activity.ISettingFragment
import ink.z31.catbooru.util.*
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

        booruName?.text = ""
        booruHost?.text = "https://"
        booruType?.setValueIndex(0)

        booruName?.setOnPreferenceChangeListener { preference, newValue ->
            preference.summary = newValue as String
            preference.setDefaultValue(newValue)
            true
        }
        booruHost?.setOnPreferenceChangeListener { preference, newValue ->
            val host = newValue as String
            if (host.isUrl()) {
                preference.setDefaultValue(host)
                preference.summary = host
            } else {
                Toast.makeText(this.activity, R.string.booruHostNotHttp, Toast.LENGTH_SHORT).show()
            }
            true
        }


        booruType?.setOnPreferenceChangeListener { preference, newValue ->
            val index = (newValue as String).toInt()
            preference.summary = resources.getStringArray(R.array.booruType)[index]
            preference.setDefaultValue(newValue)
            true
        }
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.booru_save -> {
                    val exception = createBooru(
                        title = booruName!!.text,
                        type = booruType!!.value.toInt(),
                        host = booruHost!!.text)
                    if (exception == null) {
                        Toast.makeText(
                            AppUtil.context,
                            getText(R.string.saveSucceeded),
                            Toast.LENGTH_LONG
                        ).show()
                        activity?.finish()
                    } else {
                        AlertDialog.Builder(this.activity)
                            .setTitle(R.string.error)
                            .setMessage(exception)
                            .setPositiveButton(R.string.yes) { var1, _ -> var1.cancel() }
                            .show()
                    }

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

    private fun createBooru(title: String, type: Int, host: String): String? {
        if (!host.isUrl()) {
            return getString(R.string.booruHostNotHttp)
        }
        val verifyHost = "${host}${if (host.endsWith("/")) "" else "/"}"
        val booru = Booru(
            title = title,
            type = type,
            host = verifyHost,
            favicon = ""
        )
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                // 保存数据
                val dao = AppDatabase.getDatabase(requireContext()).booruDao()
                dao.insertBooru(booru)
                EventBus.getDefault().post(EventMsg(EventType.BOORU_CHANGE))
                // 获取封面
                booru.favicon = NetUtil.getFavicon(verifyHost)
                dao.updateBooru(booru)
                EventBus.getDefault().post(EventMsg(EventType.BOORU_CHANGE))
            }
        }
        return null
    }
}