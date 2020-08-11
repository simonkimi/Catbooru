package ink.z31.catbooru.util

import android.view.View

object ViewUtils {
    @JvmStatic
    fun translationYBy(view: View, offset: Float) {
        view.translationY = view.translationY + offset
    }

    @JvmStatic
    fun getY2(view: View): Float {
        return view.bottom + view.translationY
    }
}