package ink.z31.catbooru.data.database.dao

import androidx.room.*
import ink.z31.catbooru.data.database.Booru

@Dao
interface BooruDao {
    @Insert
    fun insertBooru(booru: Booru)

    @Update
    fun updateBooru(booru: Booru)

    @Query("select * from Booru")
    fun getAllBooru(): List<Booru>

    @Delete
    fun deleteBooru(booru: Booru)
}