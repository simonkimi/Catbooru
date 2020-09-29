package ink.z31.catbooru.ui.widget.searchview

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText

class SearchBarEditText : AppCompatEditText {
    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet)
            : super(context, attributeSet)

    constructor(context: Context, attributeSet: AttributeSet, def: Int)
            : super(context, attributeSet, def)


    var listener: SearchEditTextListener? = null

    init {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                listener?.onSearchTextChanged(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }


    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        event?.let {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (event.action == KeyEvent.ACTION_DOWN && event.repeatCount == 0) {
                    keyDispatcherState.startTracking(event, this)
                    return true
                } else if (event.action == KeyEvent.ACTION_UP) {
                    keyDispatcherState.handleUpEvent(event)
                    if (event.isTracking && !event.isCanceled) {
                        listener?.let {
                            it.onSearchEditTextBackPressed()
                            return true
                        }
                    }
                }
            }
        }
        return super.onKeyPreIme(keyCode, event)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event!!.action == MotionEvent.ACTION_UP && listener != null) {
            listener?.onSearchEditTextClick()
        }
        return try {
            super.onTouchEvent(event)
        } catch (e: Throwable) {
            false
        }
    }


    interface SearchEditTextListener {
        fun onSearchEditTextClick()
        fun onSearchEditTextBackPressed()
        fun onSearchTextChanged(text: String)
    }
}