package ink.z31.catbooru.util

import android.content.res.Resources
import android.view.View

object ViewUtils {
    fun translationYBy(view: View, offset: Float) {
        view.translationY = view.translationY + offset
    }

    fun getY2(view: View): Float {
        return view.bottom + view.translationY
    }

    fun getStatusBarHeight(): Int {
        val resources = AppUtil.context.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }

}