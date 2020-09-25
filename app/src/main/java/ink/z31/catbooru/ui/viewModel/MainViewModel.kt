package ink.z31.catbooru.ui.viewModel

import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.*
import ink.z31.catbooru.data.database.*
import ink.z31.catbooru.data.model.base.BooruPost
import ink.z31.catbooru.data.model.base.BooruPostEnd
import ink.z31.catbooru.data.network.BooruNetwork
import ink.z31.catbooru.data.network.DanbooruNetwork
import ink.z31.catbooru.data.network.GelbooruNetwork
import ink.z31.catbooru.data.network.MoebooruNetwork
import ink.z31.catbooru.ui.widget.searchBar.SearchBarSuggestionsAdapter
import ink.z31.catbooru.ui.widget.searchBar.SearchSuggestion
import ink.z31.catbooru.util.AppUtil
import ink.z31.catbooru.util.NetUtil
import ink.z31.catbooru.util.SPUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.concurrent.atomic.AtomicInteger

private const val TAG = "MainViewModel"

class MainViewModel : ViewModel() {

    val booruPostList = MutableLiveData<MutableList<BooruPost>>()  // 缩略图列表
    val booruList = MutableLiveData<List<Booru>>() // Booru列表

    lateinit var booru: Booru
    private val booruListDao = AppDatabase.getDatabase(AppUtil.context).booruDao()
    private val searchHistoryDao = AppDatabase.getDatabase(AppUtil.context).searchHistoryDao()
    private val tagStoreDao = AppDatabase.getDatabase(AppUtil.context).tagStoreDao()

    // 搜索栏
    private var searchTag = ""
    val tagSuggestionAdapter = SearchBarSuggestionsAdapter(LayoutInflater.from(AppUtil.context))


    private lateinit var booruRepository: BooruRepository

    override fun onCleared() {
        Log.i(TAG, "MainViewModel已被销毁")
    }

    init {
        viewModelScope.launch {
            updateSuggestion()
        }
    }

    /**
     * 外部更新Booru列表
     */
    fun getAllBooruAsync() {
        viewModelScope.launch {
            booruList.value = booruListDao.getAllBooru()
        }
    }

    /**
     * 初始化数据库
     */
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

    /**
     * 外部初始化Booru
     */
    fun initBooruAsync(onInitSuccess: () -> Unit) {
        viewModelScope.launch {
            initBooru()
            onInitSuccess()
        }
    }

    /**
     * 初始化Booru
     */
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

    /**
     * 外部启动新的Booru
     */
    fun launchNewBooruAsync(booruId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            booru = booruListDao.getBooru(booruId)[0]
            launchNewBooru(booru)
            onSuccess()
        }
    }


    /**
     * 发起一次新的搜索
     */
    fun launchNewSearchAsync(
        tags: String = searchTag,
        onSuccess: () -> Unit,
        onEnd: () -> Unit,
        onFail: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                launchNewSearch(tags)
                onSuccess()
            } catch (e: BooruPostEnd) {
                Log.i(TAG, "加载界面, 已经到最后一面了")
                onEnd()
            } catch (e: Exception) {
                Log.i(TAG, "加载出错 launchNewSearchAsync ${e.message}")
                onFail(e.message ?: "Unknown")
            }
        }
    }


    /**
     * 加载下一面
     */
    fun launchNextPage(onSuccess: () -> Unit, onEnd: () -> Unit, onFail: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val data = booruRepository.nextPage()
                val newList = (booruPostList.value ?: mutableListOf())
                newList.addAll(data)
                booruPostList.value = newList
                onSuccess()
            } catch (e: BooruPostEnd) {
                Log.i(TAG, "加载界面, 已经到最后一面了")
                onEnd()
            } catch (e: Exception) {
                Log.i(TAG, "加载出错 launchNextPage ${e.message}")
                onFail(e.message ?: "Unknown")
            }
        }
    }

    /**
     * 启动一个新的Booru
     */
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
    }

    /**
     * 启动一个新的搜索
     */
    private suspend fun launchNewSearch(tags: String) {
        booruPostList.value = mutableListOf()
        searchTag = tags
        if (tags.trim().isNotEmpty()) {
            val history: SearchHistory? = searchHistoryDao.getAllData().find { it.data == tags }
            if (history != null) {
                history.createTime = System.currentTimeMillis()
                searchHistoryDao.update(history)
            } else {
                searchHistoryDao.insert(
                    SearchHistory(
                        data = tags,
                        createTime = System.currentTimeMillis()
                    )
                )
            }
            updateSuggestion()
        }
        booruPostList.value = booruRepository.newSearch(tags).toMutableList()
    }

    fun updateTagStore(tags: List<String>) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val storeTag = tagStoreDao.getAll().map { it.tag }
                tags.filter { !storeTag.contains(it) }
                    .filter { it.trim().isNotEmpty() }
                    .forEach {
                    tagStoreDao.insert(
                        TagStore(
                            tag = it
                        )
                    )
                }
            }
            updateSuggestion()
        }
    }

    private suspend fun updateSuggestion() {
        viewModelScope.launch {
            val suggestion = mutableListOf<SearchSuggestion>()
            suggestion.addAll(searchHistoryDao.getAllData().map { SearchSuggestion(suggestion = "__his__${it.data}") })
            suggestion.addAll(tagStoreDao.getAll().map { SearchSuggestion(suggestion = "__tag__${it.tag}") })
            tagSuggestionAdapter.suggestions = suggestion
            tagSuggestionAdapter.filter.filter(searchTag)
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
    @Volatile
    private var booruPage = AtomicInteger(0)
    private val booruLimit = 50

    suspend fun getNextPage(): List<BooruPost> {
        Log.i(TAG, "加载页面 $booruPage")
        return try {
            api.postsList(booruLimit, booruPage.getAndIncrement(), tags)
        } catch (e: BooruPostEnd) {
            throw e
        } catch (e: Exception) {
            booruPage.decrementAndGet()
            throw e
        }
    }
}