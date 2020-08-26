package ink.z31.catbooru.data.database.dao

import androidx.room.*
import ink.z31.catbooru.data.database.Booru

@Dao
interface BooruDao {
    @Insert
    suspend fun insertBooru(booru: Booru)

    @Update
    suspend fun updateBooru(booru: Booru)

    @Query("select * from Booru")
    suspend fun getAllBooru(): List<Booru>

    @Delete
    suspend fun deleteBooru(booru: Booru)
}