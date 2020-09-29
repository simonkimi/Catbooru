package ink.z31.catbooru.ui.widget.searchview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class SampleSuggestionAdapter(inflater: LayoutInflater) :
    SuggestionsAdapter<String, SampleSuggestionAdapter.SuggestionHolder>(inflater) {

    inner class SuggestionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(android.R.id.text1)
    }

    override fun onBindSuggestionHolder(
        suggestion: String?,
        holder: SuggestionHolder,
        position: Int
    ) {
        holder.textView.text = suggestion ?: ""
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionHolder {
        return SuggestionHolder(
            layoutInflater.inflate(
                android.R.layout.simple_list_item_1,
                parent,
                false
            )
        )
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val result = FilterResults()
                result.values = if (p0?.trim()?.isNotEmpty() != false) {
                    suggestions.filter { it.toLowerCase(Locale.ROOT).contains(p0!!) }
                } else {
                    suggestions
                }
                return result
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                suggestionsFiltered = p1!!.values as List<String>
                notifyDataSetChanged()
            }

        }
    }

    override fun suggestionItemHeight(): Int {
        return 144
    }
}