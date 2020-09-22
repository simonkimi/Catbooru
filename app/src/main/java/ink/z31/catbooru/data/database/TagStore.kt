package ink.z31.catbooru.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TagStore(@PrimaryKey var tag: String)