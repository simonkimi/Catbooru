package ink.z31.catbooru.ui.widget.searchview


import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import ink.z31.catbooru.R
import ink.z31.catbooru.util.AppUtil
import ink.z31.catbooru.util.ViewUtils
import kotlinx.android.synthetic.main.widget_search_bar.view.*
import kotlin.math.min


class SearchView : CardView {
    companion object {
        enum class STATE {
            STATE_MAIN,
            STATE_SEARCH
        }
    }

    constructor(context: Context)
            : super(context)

    constructor(context: Context, attributeSet: AttributeSet)
            : super(context, attributeSet)

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int)
            : super(context, attributeSet, defStyleAttr)

    val animDuration = 250L

    var leftDrawable: Drawable? = null
        set(value) {
            field = value
            searchNav.setImageDrawable(value)
        }
        get() = searchNav.drawable


    var rightDrawable: Drawable? = null
        set(value) {
            field = value
            searchAction.setImageDrawable(value)
        }
        get() = searchAction.drawable

    var editTextHint: String = ""
        set(value) {
            searchEditText.hint = value
            field = value
        }
        get() = searchEditText.hint.toString()

    var titleHint: String = ""
        set(value) {
            searchTitle.text = value
            field = value
        }
        get() = searchTitle.text.toString()

    var text: String = ""
        set(value) {
            field = value
            searchEditText.setText(value)
            searchEditText.setSelection(value.length)
        }
        get() = searchEditText.text.toString()


    var helper: Helper? = null

    var state: STATE = STATE.STATE_MAIN

    var suggestionsAdapter: SuggestionsAdapter<*, *>? = null
        set(value) {
            field = value
            suggestionRecyclerView.adapter = value
        }

    var suggestionOpen: Boolean = false
        set(value) {
            field = value
            suggestionsAdapter?.let {
                if (value && it.suggestions.isNotEmpty()) {
                    val location1 = IntArray(2)
                    this.getLocationInWindow(location1)
                    val height =
                        resources.displayMetrics.heightPixels - y - location1[1] - this.height
                    animateSuggestions(
                        0,
                        min(height.toInt(), it.suggestionItemHeight() * it.suggestionsFiltered.size)
                    )
                } else {
                    animateSuggestions(this.height, 0)
                }
            }
        }


    init {
        val layoutInflater = LayoutInflater.from(context)
        layoutInflater.inflate(R.layout.widget_search_bar, this)
        searchNav.setOnClickListener {
            helper?.onLeftButtonClick()
        }
        searchAction.setOnClickListener {
            helper?.onRightButtonClick()
        }
        searchTitle.setOnClickListener {
            helper?.onHintTextClick()
        }
        searchEditText.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEARCH || i == EditorInfo.IME_NULL) {
                helper?.onSearch(searchEditText.text.toString().trim())
                true
            } else {
                false
            }
        }
        searchEditText.listener = object : SearchBarEditText.SearchEditTextListener {
            override fun onSearchEditTextClick() {
                helper?.onSearchEditTextClick()
            }

            override fun onSearchEditTextBackPressed() {
                helper?.onSearchEditTextBackPressed()
            }

            override fun onSearchTextChanged(text: String) {
                helper?.onSearchTextChange(text)
            }

        }
        val layoutManager = LinearLayoutManager(context)
        layoutManager.stackFromEnd = true
        suggestionRecyclerView.layoutManager = layoutManager
    }

    fun setSearchState(state: STATE) {
        when (state) {
            STATE.STATE_MAIN -> {
                val imm: InputMethodManager =
                    AppUtil.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(this.windowToken, 0)
                ViewUtils.fadeByAlpha(searchEditText, animDuration)
                ViewUtils.showByAlpha(searchTitle, animDuration)
                leftDrawable = ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_animate_back_to_menu
                )
                (leftDrawable as Animatable).start()
                rightDrawable = ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_animate_fork_to_add
                )
                (rightDrawable as Animatable).start()
                suggestionOpen = false
            }
            STATE.STATE_SEARCH -> {
                ViewUtils.fadeByAlpha(searchTitle, animDuration)
                ViewUtils.showByAlpha(searchEditText, animDuration)
                leftDrawable = ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_animate_menu_to_back
                )
                (leftDrawable as Animatable).start()
                rightDrawable = ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_animate_add_to_fork
                )
                (rightDrawable as Animatable).start()
                searchEditText.requestFocus()
                suggestionOpen = true
            }

        }
        this.state = state
    }

    fun addTextChangedListener(watcher: TextWatcher) {
        searchEditText.addTextChangedListener(watcher)
    }

    fun resizeSuggestions() {
        suggestionsAdapter?.let {
            if (state == STATE.STATE_SEARCH) {
                val fitSize = it.suggestionItemHeight() * it.suggestionsFiltered.size
                val location1 = IntArray(2)
                this.getLocationInWindow(location1)
                val windowsSize =
                    resources.displayMetrics.heightPixels - y - location1[1] - this.height
                if (fitSize < windowsSize) {
                    animateSuggestions(this.height, fitSize)
                }
            }
        }
    }


    private fun animateSuggestions(from: Int, to: Int) {
        if (to > 0) {
            suggestionContainer.visibility = View.VISIBLE
        }
        if (to > 0 && suggestionsAdapter?.itemCount ?: 0 == 0) {
            return
        }
        val lp = suggestionRecyclerView.layoutParams
        val animator = ValueAnimator.ofInt(from, to)
            .setDuration(animDuration)
        animator.addUpdateListener {
            lp.height = it.animatedValue as Int
            suggestionRecyclerView.layoutParams = lp
        }
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                if (to == 0) {
                    suggestionContainer.visibility = View.GONE
                }
            }

            override fun onAnimationCancel(p0: Animator?) {

            }

            override fun onAnimationRepeat(p0: Animator?) {

            }

        })
        animator.start()
    }

    interface Helper {
        fun onSearchEditTextClick()
        fun onSearchEditTextBackPressed()
        fun onLeftButtonClick()
        fun onRightButtonClick()
        fun onSearch(key: String)
        fun onHintTextClick()
        fun onSearchTextChange(text: String)
    }
}