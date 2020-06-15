package ink.z31.catbooru.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ink.z31.catbooru.data.model.base.BooruPreviewImage

class MainModel: ViewModel() {
    val preViewImgList = MutableLiveData<MutableList<BooruPreviewImage>>()

    init {

    }
}