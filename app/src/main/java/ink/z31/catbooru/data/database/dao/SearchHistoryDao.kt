package ink.z31.catbooru.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ink.z31.catbooru.data.database.SearchHistory

@Dao
interface SearchHistoryDao {
    @Query("select * from SearchHistory")
    suspend fun getAllData(): List<SearchHistory>

    @Insert
    suspend fun insert(searchHistory: SearchHistory)

    @Update
    suspend fun update(searchHistory: SearchHistory)
}