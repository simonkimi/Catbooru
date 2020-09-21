package ink.z31.catbooru.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ink.z31.catbooru.data.model.base.BooruPost

class PostViewModel(val booruPost: BooruPost) : ViewModel() {

    class PostViewModelFactory(private val booruPost: BooruPost): ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PostViewModel(booruPost) as T
        }
    }
}