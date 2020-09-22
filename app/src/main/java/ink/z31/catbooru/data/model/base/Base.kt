package ink.z31.catbooru.data.model.base

import android.os.Parcel
import android.os.Parcelable
import ink.z31.catbooru.data.database.BooruType
import java.lang.Exception


enum class RATING(var value: Int) {
    SAFE(0x0), EXPLICIT(0x1), QUESTIONABLE(0x2);

    companion object {
        fun get(index: Int): RATING {
            val list = values().toList().filter { it.value == index }
            if (list.isEmpty()) {
                throw Exception("illegal RATING value")
            }
            return list[0]
        }
    }
}

data class BooruTag(
    val name: String,
    val count: Int,
    val type: BooruTAGType
)

data class BooruPost(
    // 基础信息
    val id: Int,
    val creatorId: String,
    // 图片地址
    val imgURL: String,
    val previewURL: String,
    val sampleURL: String,
    // 图片信息
    val width: Int,
    val height: Int,
    val sampleWidth: Int,
    val sampleHeight: Int,
    val previewWidth: Int,
    val previewHeight: Int,
    // 图片详情
    val rating: RATING,
    val status: String,
    val tags: List<String>,
    val source: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        id = parcel.readInt(),
        creatorId = parcel.readString()!!,
        imgURL = parcel.readString()!!,
        previewURL = parcel.readString()!!,
        sampleURL = parcel.readString()!!,
        width = parcel.readInt(),
        height = parcel.readInt(),
        sampleWidth = parcel.readInt(),
        sampleHeight = parcel.readInt(),
        previewWidth = parcel.readInt(),
        previewHeight = parcel.readInt(),
        rating = RATING.get(parcel.readInt()),
        status = parcel.readString()!!,
        tags = parcel.createStringArrayList()!!,
        source = parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(creatorId)
        parcel.writeString(imgURL)
        parcel.writeString(previewURL)
        parcel.writeString(sampleURL)
        parcel.writeInt(width)
        parcel.writeInt(height)
        parcel.writeInt(sampleWidth)
        parcel.writeInt(sampleHeight)
        parcel.writeInt(previewWidth)
        parcel.writeInt(previewHeight)
        parcel.writeInt(rating.value)
        parcel.writeString(status)
        parcel.writeStringList(tags)
        parcel.writeString(source)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BooruPost> {
        override fun createFromParcel(parcel: Parcel): BooruPost {
            return BooruPost(parcel)
        }

        override fun newArray(size: Int): Array<BooruPost?> {
            return arrayOfNulls(size)
        }
    }

}


enum class BooruTAGType {
    GENERAL,
    COPYRIGHT,
    ARTIST,
    CHARACTER,
    METADATA
}

class BooruPostEnd(val msg: String) : Exception()