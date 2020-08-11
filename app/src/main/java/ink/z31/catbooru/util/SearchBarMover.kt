package ink.z31.catbooru.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SearchBarMoverTest(private val searchBar: View,
                         recyclerView: RecyclerView):
    RecyclerView.OnScrollListener() {

    init {
        recyclerView.addOnScrollListener(this)
    }


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
    }

}