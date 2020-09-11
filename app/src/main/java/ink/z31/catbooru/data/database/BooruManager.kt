package ink.z31.catbooru.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

interface IBooruType {
    fun getBooruString(): String
}

enum class BooruType(val value: Int) : IBooruType {
    GELBOORU(0x0) {
        override fun getBooruString(): String {
            return "Gelbooru"
        }
    },
    DANBOORU(0x1) {
        override fun getBooruString(): String {
            return "Danbooru"
        }
    },
    MOEBOORU(0x2) {
        override fun getBooruString(): String {
            return "MoeBooru"
        }
    };

    companion object {
        fun get(index: Int): BooruType? {
            val list = values().toList().filter { it.value == index }
            return if (list.isEmpty()) null else list[0]
        }
    }
}

@Entity
data class Booru(
    var title: String,
    var host: String,
    var type: Int,
    var favicon: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}