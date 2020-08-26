package ink.z31.catbooru.util

import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceFragmentCompat
import androidx.fragment.app.Fragment


interface ISettingFragment {
    fun getMenuRes(): Int?
}




abstract class SettingPreferenceFragment: PreferenceFragmentCompat(), ISettingFragment

abstract class SettingBaseFragment: Fragment(), ISettingFragment