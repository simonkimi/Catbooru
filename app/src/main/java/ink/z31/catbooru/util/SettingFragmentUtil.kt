package ink.z31.catbooru.util

import androidx.preference.PreferenceFragmentCompat


interface ISettingFragment {
    fun getMenu(): Int
}




abstract class SettingPreferenceFragment: PreferenceFragmentCompat(), ISettingFragment