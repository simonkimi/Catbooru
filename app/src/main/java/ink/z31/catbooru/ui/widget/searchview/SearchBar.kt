package ink.z31.catbooru.ui.widget.searchview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import ink.z31.catbooru.R

class SearchView(context: Context, attrs: AttributeSet) : CardView(context, attrs) {
    init {
        LayoutInflater.from(context).inflate(R.layout.widget_search_bar, this)
    }
}