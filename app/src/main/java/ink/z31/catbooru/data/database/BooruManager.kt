package ink.z31.catbooru.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey


enum class BooruType(val BooruType: Int) {
    GELBOORU(0x0),
    DANBOORU(0x1)
}

@Entity
data class Booru(
    var name: String,
    var url: String,
    var type: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}