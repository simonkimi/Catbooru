package ink.z31.catbooru.ui.widget.searchview

import android.view.LayoutInflater
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView

abstract class SuggestionsAdapter<T, V : RecyclerView.ViewHolder>(val layoutInflater: LayoutInflater) :
    RecyclerView.Adapter<V>(),
    Filterable {
    var suggestionsFiltered = listOf<T>()

    var suggestions = listOf<T>()
        set(value) {
            field = value
            suggestionsFiltered = value
            notifyDataSetChanged()
        }


    override fun onBindViewHolder(holder: V, position: Int) {
        onBindSuggestionHolder(suggestionsFiltered[position], holder, position)
    }

    override fun getItemCount(): Int {
        return suggestionsFiltered.size
    }

    abstract fun onBindSuggestionHolder(suggestion: T?, holder: V, position: Int)


    abstract fun suggestionsMaxShow(): Int
}