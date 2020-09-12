package ink.z31.catbooru.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object SPUtil {
    @Suppress("UNCHECKED_CAST")
    fun <T> get(file: String, key: String, default: T): T =
        AppUtil.context.getSharedPreferences(file, Context.MODE_PRIVATE).run {
            when (default) {
                is String -> getString(key, default) as T? ?: default
                is Int -> getInt(key, default) as T? ?: default
                is Boolean -> getBoolean(key, default) as T? ?: default
                is Long -> getLong(key, default) as T? ?: default
                else -> throw Exception("Unknown")
            }
        }

    fun set(file: String, block: SharedPreferences.Editor.() -> Unit) {
        val editor = AppUtil.context.getSharedPreferences(file, Context.MODE_PRIVATE).edit()
        editor.block()
        editor.apply()
    }
}