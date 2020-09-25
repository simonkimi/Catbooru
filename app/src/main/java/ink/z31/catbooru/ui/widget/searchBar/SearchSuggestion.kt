package ink.z31.catbooru.ui.widget.searchBar

import android.os.Parcel
import android.os.Parcelable

data class SearchSuggestion(
    var suggestion: String
) : Parcelable {
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