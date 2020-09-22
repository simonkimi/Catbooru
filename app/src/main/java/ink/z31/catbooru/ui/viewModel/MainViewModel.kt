package ink.z31.catbooru.ui.viewModel

import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.*
import ink.z31.catbooru.data.database.AppDatabase
import ink.z31.catbooru.data.database.Booru
import ink.z31.catbooru.data.database.BooruType
import ink.z31.catbooru.data.database.SearchHistory
import ink.z31.catbooru.data.model.base.BooruPost
import ink.z31.catbooru.data.model.base.BooruPostEnd
import ink.z31.catbooru.data.network.BooruNetwork
import ink.z31.catbooru.data.network.DanbooruNetwork
import ink.z31.catbooru.data.network.GelbooruNetwork
import ink.z31.catbooru.data.network.MoebooruNetwork
import ink.z31.catbooru.ui.widget.searchBar.SearchBarSuggestionsAdapter
import ink.z31.catbooru.util.AppUtil
import ink.z31.catbooru.util.NetUtil
import ink.z31.catbooru.util.SPUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

private const val TAG = "MainViewModel"

class MainViewModel : ViewModel() {

    val booruPostList = MutableLiveData<MutableList<BooruPost>>()  // 缩略图列表
    val booruPostEnd = MutableLiveData<Boolean>()  // 是否到最后一面
    val progressBarVis = MutableLiveData<Boolean>()  // 是否正在加载
    val booruList = MutableLiveData<List<Booru>>() // Booru列表

    lateinit var booru: Booru


    private val booruListDao = AppDatabase.getDatabase(AppUtil.context).booruDao()
    private val searchHistoryDao = AppDatabase.getDatabase(AppUtil.context).searchHistoryDao()

    // 搜索栏
    private var searchTag = ""
    val historySuggestionAdapter = SearchBarSuggestionsAdapter(LayoutInflater.from(AppUtil.context))
    val tagSuggestionAdapter = SearchBarSuggestionsAdapter(LayoutInflater.from(AppUtil.context))


    private lateinit var booruRepository: BooruRepository

    init {
        viewModelScope.launch {
            initBooru()
            booruPostEnd.value = false
        }
    }

    override fun onCleared() {
        Log.i(TAG, "MainViewModel已被销毁")
    }

    fun getAllBooruAsync() {
        viewModelScope.launch {
            booruList.value = booruListDao.getAllBooru()
        }
    }

    private suspend fun initDatabase(): List<Booru> {
        val booruList = booruListDao.getAllBooru()
        return if (booruList.isEmpty()) {
            val booru = Booru(
                title = "Gelbooru",
                host = "https://gelbooru.com",
                type = BooruType.GELBOORU.value,
                favicon = ""
            )
            booruListDao.insertBooru(booru)
            booruListDao.getAllBooru()
        } else {
            booruList
        }
    }

    private suspend fun initBooru() {
        val list = initDatabase()
        val defaultId = SPUtil.get("main", "start_booru_id", 0L)
        val defaultBooru = list.filter { it.id == defaultId }
        booru = if (defaultBooru.isNotEmpty()) {
            defaultBooru[0]
        } else {
            list[0]
        }
        booruList.value = list
        launchNewBooru(booru)
    }


    fun launchNewBooruAsync(booruId: Int) {
        viewModelScope.launch {
            booru = booruListDao.getBooru(booruId)[0]
            launchNewBooru(booru)
        }
    }


    /**
     * 发起一次新的搜索
     */
    fun launchNewSearchAsync(tags: String) {
        viewModelScope.launch {
            launchNewSearch(tags)
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
            } catch (e: Exception) {
                Log.i(TAG, "加载界面, 已经到最后一面了")
                booruPostEnd.value = true
            }
        }
    }


    private suspend fun launchNewBooru(booru: Booru) {
        withContext(Dispatchers.IO) {
            if (booru.favicon.isEmpty()) {
                val icon = NetUtil.getFavicon(booru.host)
                if (icon.isNotEmpty()) {
                    booru.favicon = icon
                }
                booruListDao.updateBooru(booru)

            }
        }
        this.booruRepository = BooruRepository(booru)
        this.launchNewSearch(searchTag)
    }

    private suspend fun launchNewSearch(tags: String) {
        booruPostList.value = mutableListOf()
        progressBarVis.value = true
        searchTag = tags
        searchHistoryDao.insert(SearchHistory(
            data = tags,
            createTime = System.currentTimeMillis()
        ))
        try {
            val newSearchList = booruRepository.newSearch(tags)
            booruPostList.value = newSearchList.toMutableList()
            booruPostEnd.value = false
        } catch (e: BooruPostEnd) {
            Log.i(TAG, "加载界面, 已经到最后一面了")
            booruPostEnd.value = true
        } catch (e: Exception) {
            Log.e(TAG, "加载页面发生错误${e}")
            e.printStackTrace()
        } finally {
            progressBarVis.value = false
        }
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
        val data = this.api.postsList(booruLimit, booruPage, tags)
        booruPage += 1
        return data
    }
}