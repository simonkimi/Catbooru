package ink.z31.catbooru.util

import android.animation.Animator
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

    fun <T : View> fadeByAlpha(view: T, duration: Long) {
        view.animate()
            .alphaBy(1F)
            .alpha(0F)
            .setDuration(duration)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator?) {

                }

                override fun onAnimationEnd(p0: Animator?) {
                    view.visibility = View.GONE
                }

                override fun onAnimationCancel(p0: Animator?) {

                }

                override fun onAnimationRepeat(p0: Animator?) {

                }

            })
            .start()
    }

    fun <T : View> showByAlpha(view: T, duration: Long) {
        view.visibility = View.VISIBLE
        view.animate()
            .alphaBy(0F)
            .alpha(1F)
            .setDuration(duration)
            .setListener(null)
            .start()
    }

}