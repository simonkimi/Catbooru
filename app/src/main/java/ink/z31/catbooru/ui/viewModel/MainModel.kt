package ink.z31.catbooru.ui.viewModel

import android.provider.Settings
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import ink.z31.catbooru.data.database.Booru
import ink.z31.catbooru.data.database.BooruType
import ink.z31.catbooru.data.model.base.booruApiCreator
import ink.z31.catbooru.data.model.base.BooruPreviewImage
import ink.z31.catbooru.data.model.gelbooru.GelbooruServer
import kotlinx.coroutines.Dispatchers

class MainModel : ViewModel() {
    val preViewImgList = MutableLiveData<MutableList<BooruPreviewImage>>()

    init {

    }
}

class Repository(booru: Booru) {
    val booruApi = when(booru.type) {
        BooruType.GELBOORU.ordinal -> booruApiCreator<GelbooruServer>(booru.url)
        else -> booruApiCreator<GelbooruServer>(booru.url)
    }





}