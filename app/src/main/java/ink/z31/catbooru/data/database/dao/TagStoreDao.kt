package ink.z31.catbooru.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ink.z31.catbooru.data.database.TagStore

@Dao
interface TagStoreDao {
    @Query("select * from TagStore where tag like :start || '%'")
    suspend fun getStartWith(start: String): List<TagStore>

    @Insert
    suspend fun insert(tagStore: TagStore)
}