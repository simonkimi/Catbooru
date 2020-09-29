package ink.z31.catbooru.ui.activity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import ink.z31.catbooru.R
import ink.z31.catbooru.ui.widget.searchview.SampleSuggestionAdapter
import ink.z31.catbooru.ui.widget.searchview.SearchView
import ink.z31.catbooru.util.AppUtil
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        val adapter = SampleSuggestionAdapter(LayoutInflater.from(applicationContext))
        adapter.suggestions = listOf(
            "Hello",
            "Welcome",
            "To",
            "Catbooru",
            "Hello",
            "Welcome",
            "To",
            "Catbooru",
            "Hello",
            "Welcome",
            "To",
            "Catbooru",
            "Hello",
            "Welcome",
            "To",
            "Catbooru",
            "Hello",
            "Welcome",
            "To",
            "Catbooru"
        )
        searchView.suggestionsAdapter = adapter
        searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                adapter.filter.filter(p0)
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        val searchHelper = object : SearchView.Helper {
            override fun onSearchEditTextClick() {

            }

            override fun onSearchEditTextBackPressed() {
                println("onSearchEditTextBackPressed")
                if (searchView.state == SearchView.Companion.STATE.STATE_SEARCH) {
                    searchView.setSearchState(SearchView.Companion.STATE.STATE_MAIN)
                }
            }

            override fun onLeftButtonClick() {
                if (searchView.state == SearchView.Companion.STATE.STATE_SEARCH) {
                    searchView.setSearchState(SearchView.Companion.STATE.STATE_MAIN)
                }
            }

            override fun onRightButtonClick() {

            }

            override fun onSearch(key: String) {

            }

            override fun onHintTextClick() {
                searchView.setSearchState(SearchView.Companion.STATE.STATE_SEARCH)
                val imm: InputMethodManager =
                    AppUtil.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
            }

            override fun onSearchTextChange(text: String) {
                TODO("Not yet implemented")
            }

        }
        searchView.helper = searchHelper
    }

    override fun onBackPressed() {
        if (searchView.suggestionOpen) {
            println("返回前关闭")
            if (searchView.state == SearchView.Companion.STATE.STATE_SEARCH) {
                searchView.setSearchState(SearchView.Companion.STATE.STATE_MAIN)
                searchView.suggestionOpen = false
            }
        } else {
            super.onBackPressed()
        }
    }


}