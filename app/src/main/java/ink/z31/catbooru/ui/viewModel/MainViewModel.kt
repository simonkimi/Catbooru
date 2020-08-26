package ink.z31.catbooru.ui.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import ink.z31.catbooru.data.database.AppDatabase
import ink.z31.catbooru.data.database.Booru
import ink.z31.catbooru.data.database.BooruType
import ink.z31.catbooru.data.model.base.BooruPost
import ink.z31.catbooru.data.model.base.BooruPostEnd
import ink.z31.catbooru.data.network.BooruNetwork
import ink.z31.catbooru.data.network.DanbooruNetwork
import ink.z31.catbooru.data.network.GelbooruNetwork
import ink.z31.catbooru.data.network.MoebooruNetwork
import kotlinx.coroutines.launch
import java.lang.Exception

private const val TAG = "MainViewModel"

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val booruDao = AppDatabase.getDatabase(getApplication()).booruDao()

    val booruPostList = MutableLiveData<MutableList<BooruPost>>()  // 缩略图列表
    val booruPostEnd = MutableLiveData<Boolean>()  // 是否到最后一面
    val progressBarVis = MutableLiveData<Boolean>()  // 是否正在加载
    val booruList = MutableLiveData<MutableList<Booru>>() // booru列表


    private lateinit var booruRepository: BooruRepository

    init {
        booruPostList.value = mutableListOf()
        booruPostEnd.value = false


        val booru =
            Booru(
                name = "Moebooru",
                host = "https://gelbooru.com/",
                type = BooruType.GELBOORU.value
            )
        launchNewBooru(booru)

        viewModelScope.launch {
            booruList.value = getBooru()
        }
    }

    suspend fun getBooru(): MutableList<Booru> {
        return booruDao.getAllBooru().toMutableList()
    }

    /**
     * 创建新的Booru引擎
     */
    fun launchNewBooru(booru: Booru) {
        this.booruRepository = BooruRepository(booru)
        this.launchNewSearch("")
    }


    /**
     * 发起一次新的搜索
     */
    fun launchNewSearch(tags: String) {
        viewModelScope.launch {
            progressBarVis.value = true
            try {
                val newSearchList = booruRepository.newSearch(tags)
                booruPostList.value = newSearchList.toMutableList()
                booruPostEnd.value = false
            } catch (e: BooruPostEnd) {
                Log.i(TAG, "加载界面, 已经到最后一面了")
                booruPostEnd.value = true
            } catch (e: Exception) {
                Log.e(TAG, "加载页面发生错误${e.stackTrace}")
            } finally {
                progressBarVis.value = false
            }

        }
    }

    /**
     * 加载下一面
     */
    fun launchNextPage() {
        viewModelScope.launch {
            try {
                val data = booruRepository.nextPage()
                val newList = (booruPostList.value ?: mutableListOf())
                newList.addAll(data)
                booruPostList.value = newList
                booruPostEnd.value = false
            } catch (e: BooruPostEnd) {
                Log.i(TAG, "加载界面, 已经到最后一面了")
                booruPostEnd.value = true
            }
        }
    }
}


class MainViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(application) as T
    }

}


class BooruRepository(booru: Booru) {
    private val booruApi = when (booru.type) {
        BooruType.GELBOORU.value -> GelbooruNetwork(booru)
        BooruType.DANBOORU.value -> DanbooruNetwork(booru)
        BooruType.MOEBOORU.value -> MoebooruNetwork(booru)
        else -> GelbooruNetwork(booru)
    }

    private var booruPostList = BooruPostList(booruApi, "")

    suspend fun newSearch(tags: String): List<BooruPost> {
        booruPostList = BooruPostList(booruApi, tags)
        return booruPostList.getNextPage()
    }

    suspend fun nextPage(): List<BooruPost> {
        return booruPostList.getNextPage()
    }
}


class BooruPostList(private val api: BooruNetwork, private val tags: String) {
    private var booruPage = 0
    private val booruLimit = 50

    suspend fun getNextPage(): List<BooruPost> {
        val data = try {
            this.api.postsList(booruLimit, booruPage, tags)
        } catch (e: Exception) {
            throw e
        }
        booruPage += 1
        return data
    }
}