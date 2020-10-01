package ink.z31.catbooru.ui.widget.searchview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ink.z31.catbooru.R
import kotlin.math.min


class SearchBarSuggestionsAdapter(inflater: LayoutInflater) :
    SuggestionsAdapter<String, SearchBarSuggestionsAdapter.SuggestionHolder>(
        inflater
    ) {

    var suggestionHelper: SuggestionHelper? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionHolder {
        val view: View = layoutInflater.inflate(R.layout.item_list_sample, parent, false)
        return SuggestionHolder(view)
    }


    override fun onBindSuggestionHolder(
        suggestion: String?,
        holder: SuggestionHolder,
        position: Int
    ) {
        holder.suggestionTextView.text = suggestion!!
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val input = p0!!.toString()
                val results = FilterResults()
                var res = if (input.trim().isEmpty()) {
                    suggestions
                        .filter { it.startsWith("__his__") }
                        .map {
                            it.substring("__his__".length, it.length)
                        }
                } else if (input.endsWith(" ")){
                    suggestions
                        .filter { it.startsWith("__tag__") }
                        .map { it.substring("__tag__".length, it.length) }
                } else {
                    val tags = input.split(" ")
                    val lastTag = tags.last()
                    suggestions
                        .filter { it.startsWith("__tag__") }
                        .map { it.substring("__tag__".length, it.length) }
                        .filter { !tags.contains(it) }
                        .filter { it.contains(lastTag) }
                }
                res = res.subList(0, min(suggestionsMaxShow(), res.size))
                results.values = res
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                suggestionsFiltered = p1!!.values as List<String>
                notifyDataSetChanged()
            }

        }
    }

    inner class SuggestionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var suggestionTextView: TextView = itemView.findViewById(android.R.id.text1)

        init {
            itemView.setOnClickListener {
                suggestionHelper?.onSuggestionClick(suggestionsFiltered[adapterPosition], adapterPosition)
            }
        }
    }

    interface SuggestionHelper {
        fun onSuggestionClick(suggestion: String, position: Int)
    }

    override fun suggestionsMaxShow(): Int {
        return 100
    }
}
