package ink.z31.catbooru.ui.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ink.z31.catbooru.data.database.AppDatabase
import ink.z31.catbooru.data.database.Booru
import ink.z31.catbooru.data.database.BooruType
import ink.z31.catbooru.util.AppUtil
import kotlinx.coroutines.launch

private const val TAG = "BooruViewModel"

class BooruViewModel : ViewModel() {
    private val booruDao = AppDatabase.getDatabase(AppUtil.context).booruDao()
    val booruList = MutableLiveData<MutableList<Booru>>() // booru列表

    init {
        getAllBooruAsync()
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(TAG, "BooruViewModel已销毁")
    }

    /**
     * 刷新Booru数据
     */
    fun getAllBooruAsync() {
        viewModelScope.launch {
            booruList.value = getBooruList()
        }
    }


    /**
     * 删除Booru
     */
    fun deleteBooruAsync(booru: Booru) {
        viewModelScope.launch {
            deleteBooru(booru)
            booruList.value = getBooruList()
            if (booruList.value!!.size == 0) {
                createBooru(
                    title = "Example",
                    host = "https://example.app",
                    type = BooruType.GELBOORU.value
                )
                booruList.value = getBooruList()
            }
        }
    }


    private suspend fun getBooruList(): MutableList<Booru>? {
        return booruDao.getAllBooru().toMutableList()
    }

    private suspend fun deleteBooru(booru: Booru) {
        booruDao.deleteBooru(booru)
    }


    private suspend fun createBooru(title: String, host: String, type: Int) {
        val booru = Booru(
            title = title,
            host = host,
            type = type,
            favicon = ""
        )
        booruDao.insertBooru(booru)
    }
}