package ink.z31.catbooru.ui.widget.searchview

import android.annotation.SuppressLint
import android.content.Context
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText

class SearchBarEditText(context: Context) : AppCompatEditText(context) {
    private var mListener: SearchEditTextListener? = null

    fun setSearchEditTextListener(listener: SearchEditTextListener) {
        mListener = listener
    }

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        event?.let {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (event.action == KeyEvent.ACTION_DOWN && event.repeatCount == 0) {
                    keyDispatcherState.startTracking(event, this)
                    return true
                } else if (event.action == KeyEvent.ACTION_UP) {
                    keyDispatcherState.handleUpEvent(event)
                }
                if (event.isTracking && !event.isCanceled) {
                    if (mListener != null) {
                        mListener!!.onBackPressed()
                        return true
                    }
                }
            }
        }
        return super.onKeyPreIme(keyCode, event)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event!!.action == MotionEvent.ACTION_UP && mListener != null) {
            mListener!!.onClick()
        }
        return try {
            super.onTouchEvent(event)
        } catch (e: Throwable) {
            false
        }
    }


    interface SearchEditTextListener {
        fun onClick()
        fun onBackPressed()
    }
}