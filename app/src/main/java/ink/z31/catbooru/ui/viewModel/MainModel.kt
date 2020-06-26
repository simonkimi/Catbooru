package ink.z31.catbooru.ui.viewModel

import androidx.lifecycle.*
import ink.z31.catbooru.data.database.Booru
import ink.z31.catbooru.data.database.BooruType
import ink.z31.catbooru.data.model.base.BooruPost
import ink.z31.catbooru.data.network.GelbooruNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class MainModel : ViewModel() {
    val booruPostList = MutableLiveData<MutableList<BooruPost>>()

    init {
        booruPostList.value = mutableListOf()
    }

    private val booru = Booru(name = "Gelbooru", url = "https://gelbooru.com", type = BooruType.GELBOORU.ordinal)
    private val booruRepository = Repository(booru)

    fun test() {
        viewModelScope.launch {
            val data = booruRepository.booruApi.postsList(40, 1, "")
            val newList = (booruPostList.value ?: mutableListOf())
            newList.addAll(data)
            booruPostList.value = newList
        }
    }

}

class Repository(booru: Booru) {
    val booruApi = when (booru.type) {
        BooruType.GELBOORU.ordinal -> GelbooruNetwork(booru)
        else -> GelbooruNetwork(booru)
    }


    private fun <T> fire(
        context: CoroutineContext,
        block: suspend () -> Result<T>
    ): LiveData<Result<T>> =
        liveData(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }
}