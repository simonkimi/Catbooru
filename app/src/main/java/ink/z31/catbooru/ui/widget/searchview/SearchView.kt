package ink.z31.catbooru.ui.widget.searchview


import android.animation.Animator
import android.content.Context
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import ink.z31.catbooru.R
import kotlinx.android.synthetic.main.widget_search_bar.view.*

class SearchView : CardView, SearchBarEditText.SearchEditTextListener {
    companion object {
        enum class Anim {
            MENU_TO_BACK,
            BACK_TO_MENU,
            ADD_TO_FORK,
            FORK_TO_ADD,
            SEARCH_EDIT_SHOW,
            SEARCH_EDIT_HIDE,
            SEARCH_TEXT_SHOW,
            SEARCH_TEXT_HIDE
        }

        enum class SearchState {
            DEFAULT,
            ON_SEARCH
        }
    }

    constructor(context: Context)
            : super(context)

    constructor(context: Context, attributeSet: AttributeSet)
            : super(context, attributeSet)

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int)
            : super(context, attributeSet, defStyleAttr)

    var leftDrawable: Drawable? = searchNav.drawable
        set(value) {
            field = value
            searchNav.setImageDrawable(value)
        }
        get() = searchNav.drawable


    var rightDrawable: Drawable? = searchAction.drawable
        set(value) {
            field = value
            searchAction.setImageDrawable(value)
        }
        get() = searchAction.drawable

    var editTextHint: String = "搜索 Catbooru"
        set(value) {
            searchEditText.hint = value
            field = value
        }

    var titleHint: String = "Catbooru"
        set(value) {
            searchTitle.text = value
            field = value
        }


    var searchState: SearchState = SearchState.DEFAULT

    var mHelper: Helper? = null


    init {
        val layoutInflater = LayoutInflater.from(context)
        layoutInflater.inflate(R.layout.widget_search_bar, this)
        searchNav.setOnClickListener {
            mHelper?.onLeftButtonClick()
        }
        searchAction.setOnClickListener {
            mHelper?.onRightButtonClick()
        }
        searchTitle.setOnClickListener {
            mHelper?.onHintTextClick()
        }
        searchEditText.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEARCH || i == EditorInfo.IME_NULL) {
                mHelper?.onSearch(searchEditText.text.toString().trim())
                true
            } else {
                false
            }
        }

    }

    fun playInnerAnim(state: Anim) {
        when (state) {
            Anim.MENU_TO_BACK -> {
                searchNav.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_animate_menu_to_back
                    )
                )
                (searchNav.drawable as Animatable).start()
            }
            Anim.BACK_TO_MENU -> {
                searchNav.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_animate_back_to_menu
                    )
                )
                (searchNav.drawable as Animatable).start()
            }
            Anim.SEARCH_TEXT_HIDE -> {
                searchTitle.animate()
                    .alphaBy(1F)
                    .alpha(0F)
                    .setDuration(250)
                    .setListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(p0: Animator?) {

                        }

                        override fun onAnimationEnd(p0: Animator?) {
                            searchTitle.visibility = View.GONE
                        }

                        override fun onAnimationCancel(p0: Animator?) {

                        }

                        override fun onAnimationRepeat(p0: Animator?) {

                        }

                    })
            }
            Anim.SEARCH_TEXT_SHOW -> {
                searchTitle.visibility = View.VISIBLE
                searchTitle.animate()
                    .alphaBy(0F)
                    .alpha(1F)
                    .setDuration(250)
                    .setListener(null)
                    .start()
            }
            Anim.ADD_TO_FORK -> {
                searchAction.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_animate_add_to_fork
                    )
                )
                (searchAction.drawable as Animatable).start()
            }
            Anim.FORK_TO_ADD -> {
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_animate_fork_to_add
                )
                (searchAction.drawable as Animatable).start()
            }
            Anim.SEARCH_EDIT_HIDE -> {
                searchEditText.animate()
                    .alphaBy(1F)
                    .alpha(0F)
                    .setDuration(250)
                    .setListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(p0: Animator?) {

                        }

                        override fun onAnimationEnd(p0: Animator?) {
                            searchEditText.visibility = View.GONE
                        }

                        override fun onAnimationCancel(p0: Animator?) {

                        }

                        override fun onAnimationRepeat(p0: Animator?) {

                        }

                    })
                    .start()
            }
            Anim.SEARCH_EDIT_SHOW -> {
                searchEditText.visibility = View.VISIBLE
                searchEditText.animate()
                    .setListener(null)
                    .alphaBy(0F)
                    .alpha(1F)
                    .setDuration(250)
                    .start()

            }
        }


    }

    override fun onSearchEditTextClick() {
        mHelper?.onSearchEditTextClick()
    }

    override fun onSearchEditTextBackPressed() {
        mHelper?.onSearchEditTextBackPressed()
    }

    interface Helper {
        fun onSearchEditTextClick()
        fun onSearchEditTextBackPressed()
        fun onLeftButtonClick()
        fun onRightButtonClick()
        fun onSearch(key: String)
        fun onHintTextClick()

    }
}