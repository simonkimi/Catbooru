package ink.z31.catbooru.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchHistory(
    var data: String,
    var createTime: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}