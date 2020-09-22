package ink.z31.catbooru.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ink.z31.catbooru.data.database.SearchHistory

@Dao
interface SearchHistoryDao {
    @Query("select * from SearchHistory")
    suspend fun getAllData(): List<SearchHistory>

    @Insert
    suspend fun insert(searchHistory: SearchHistory)
}