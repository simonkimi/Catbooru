package ink.z31.catbooru.data.model.base

import java.lang.Exception


enum class RATING {
    SAFE, EXPLICIT, QUESTIONABLE
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
)


enum class BooruTAGType {
    GENERAL,
    COPYRIGHT,
    ARTIST,
    CHARACTER,
    METADATA
}

class BooruPostEnd(val msg: String): Exception()