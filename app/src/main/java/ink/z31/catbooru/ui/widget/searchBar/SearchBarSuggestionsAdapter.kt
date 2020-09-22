package ink.z31.catbooru.ui.widget.searchBar

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter
import ink.z31.catbooru.R
import java.util.*

data class SearchSuggestion(
    var suggestion: String
): Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(suggestion)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SearchSuggestion> {
        override fun createFromParcel(parcel: Parcel): SearchSuggestion {
            return SearchSuggestion(parcel)
        }

        override fun newArray(size: Int): Array<SearchSuggestion?> {
            return arrayOfNulls(size)
        }
    }

}


class SearchBarSuggestionsAdapter(inflater: LayoutInflater) :
    SuggestionsAdapter<SearchSuggestion, SearchBarSuggestionsAdapter.SuggestionHolder>(
        inflater
    ) {

    private var listener: OnSuggestionClickListener? = null

    fun setOnSuggestionClickListener(listener: OnSuggestionClickListener) {
        this.listener = listener
    }


    override fun getSingleViewHeight(): Int {
        return 100
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
        holder?.suggestionTextView?.text = suggestion?.suggestion
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val results = FilterResults()
                val term: List<String> = p0!!.split("")
                suggestions = if (term.isEmpty()) {
                    suggestions_clone
                } else {
                    suggestions_clone.filter {
                        it.suggestion.toLowerCase(Locale.ROOT).contains(term[term.lastIndex])
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
                listener?.onClick(suggestions[adapterPosition], adapterPosition)
            }
        }
    }

    interface OnSuggestionClickListener {
        fun onClick(suggestion: SearchSuggestion, position: Int)
    }
}
