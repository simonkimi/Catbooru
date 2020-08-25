package ink.z31.catbooru.ui.widget.recyclerView

import android.animation.Animator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ink.z31.catbooru.util.MathUtils.clamp
import ink.z31.catbooru.util.ViewUtils.getY2
import ink.z31.catbooru.util.ViewUtils.translationYBy

class SearchBarMover(
    private val mHelper: Helper,
    private val mSearchBar: View,
    vararg recyclerViews: RecyclerView
) : RecyclerView.OnScrollListener() {
    private var mShow = false
    private var mSearchBarMoveAnimator: ValueAnimator? = null

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE && mHelper.isValidView(recyclerView)) {
            returnSearchBarPosition()
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (mHelper.isValidView(recyclerView)) {
            val oldBottom = getY2(mSearchBar).toInt()
            val offsetYStep = clamp(-dy, -oldBottom, (-mSearchBar.translationY).toInt())
            if (offsetYStep != 0) {
                translationYBy(mSearchBar, offsetYStep.toFloat())
            }
        }
    }

    @JvmOverloads
    fun returnSearchBarPosition(animation: Boolean = true) {
        if (mSearchBar.height == 0) {
            return
        }
        val show: Boolean
        show = if (mHelper.forceShowSearchBar()) {
            true
        } else {
            val recyclerView = mHelper.validRecyclerView ?: return
            if (!recyclerView.isShown) {
                true
            } else if (recyclerView.computeVerticalScrollOffset() < mSearchBar.bottom) {
                true
            } else {
                getY2(mSearchBar).toInt() > mSearchBar.height / 2
            }
        }
        val offset: Int
        offset = if (show) {
            (-mSearchBar.translationY).toInt()
        } else {
            (-getY2(mSearchBar)).toInt()
        }
        if (offset == 0) {
            // No need to scroll
            return
        }
        if (animation) {
            if (mSearchBarMoveAnimator != null) {
                mSearchBarMoveAnimator = if (mShow == show) {
                    // The same target, no need to do animation
                    return
                } else {
                    // Cancel it
                    mSearchBarMoveAnimator!!.cancel()
                    null
                }
            }
            mShow = show
            val va = ValueAnimator.ofInt(0, offset)
            va.duration = ANIMATE_TIME
            va.addListener(object : SimpleAnimatorListener() {
                override fun onAnimationEnd(animation: Animator) {
                    mSearchBarMoveAnimator = null
                }
            })
            va.addUpdateListener(object : AnimatorUpdateListener {
                var lastValue = 0
                override fun onAnimationUpdate(animation: ValueAnimator) {
                    val value = animation.animatedValue as Int
                    val offsetStep = value - lastValue
                    lastValue = value
                    translationYBy(mSearchBar, offsetStep.toFloat())
                }
            })
            mSearchBarMoveAnimator = va
            va.start()
        } else {
            mSearchBarMoveAnimator?.cancel()
            translationYBy(mSearchBar, offset.toFloat())
        }
    }

    interface Helper {
        fun isValidView(recyclerView: RecyclerView?): Boolean
        val validRecyclerView: RecyclerView?
        fun forceShowSearchBar(): Boolean
    }

    companion object {
        private const val ANIMATE_TIME = 300L
    }

    init {
        for (recyclerView in recyclerViews) {
            recyclerView.addOnScrollListener(this)
        }
    }
}

internal abstract class SimpleAnimatorListener : Animator.AnimatorListener {
    override fun onAnimationStart(animation: Animator) {}
    override fun onAnimationEnd(animation: Animator) {}
    override fun onAnimationCancel(animation: Animator) {}
    override fun onAnimationRepeat(animation: Animator) {}
}