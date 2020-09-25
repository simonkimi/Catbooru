package ink.z31.catbooru.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TagStore(var tag: String) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}