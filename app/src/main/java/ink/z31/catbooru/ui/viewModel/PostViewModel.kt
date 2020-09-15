package ink.z31.catbooru.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ink.z31.catbooru.data.model.base.BooruPost

class PostViewModel : ViewModel() {
    lateinit var booruPost: BooruPost

    fun init(booruPost: BooruPost) {
        this.booruPost = booruPost
    }
}