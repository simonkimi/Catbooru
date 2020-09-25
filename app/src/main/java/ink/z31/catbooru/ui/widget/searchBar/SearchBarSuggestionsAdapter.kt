package ink.z31.catbooru.ui.widget.searchBar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter
import ink.z31.catbooru.R
import java.util.*


class SearchBarSuggestionsAdapter(inflater: LayoutInflater) :
    SuggestionsAdapter<SearchSuggestion, SearchBarSuggestionsAdapter.SuggestionHolder>(
        inflater
    ) {

    private var listener: OnSuggestionClickListener? = null

    fun setOnSuggestionClickListener(listener: OnSuggestionClickListener): SearchBarSuggestionsAdapter {
        this.listener = listener
        return this
    }


    override fun getSingleViewHeight(): Int {
        return 2000
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionHolder {
        val view: View = layoutInflater.inflate(R.layout.item_list_sample, parent, false)
        return SuggestionHolder(view)
    }

    override fun onBindSuggestionHolder(
        suggestion: SearchSuggestion?,
        holder: SuggestionHolder?,
        position: Int
    ) {
        holder?.suggestionTextView?.text = suggestion!!.suggestion
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val input = p0!!.toString()
                val results = FilterResults()
                suggestions = if (input.trim().isEmpty()) {
                    suggestions_clone
                        .filter { it.suggestion.startsWith("__his__") }
                        .map {
                            SearchSuggestion(
                                suggestion = it.suggestion.substring(
                                    "__his__".length,
                                    it.suggestion.length
                                )
                            )
                        }
                } else {
                    val lastTag = input.split(" ").last()
                    suggestions_clone
                        .filter { it.suggestion.startsWith("__tag__") }
                        .filter { it.suggestion.contains(lastTag) }
                        .map {
                            SearchSuggestion(
                                suggestion = it.suggestion.substring(
                                    "__tag__".length,
                                    it.suggestion.length
                                )
                            )
                        }
                }
                results.values = suggestions
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                suggestions = p1!!.values as List<SearchSuggestion>
            }

        }
    }

    inner class SuggestionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var suggestionTextView: TextView = itemView.findViewById(android.R.id.text1)

        init {
            itemView.setOnClickListener {
                println(adapterPosition)
                listener?.onClick(suggestions[adapterPosition], adapterPosition)
            }
        }
    }

    interface OnSuggestionClickListener {
        fun onClick(suggestion: SearchSuggestion, position: Int)
    }
}
