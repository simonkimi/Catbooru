package ink.z31.catbooru.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey


enum class BooruType(var type: Int) {
    GELBOORU(0x0)
}

@Entity
data class Booru(
    var name: String,
    var url: String,
    var type: BooruType
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}