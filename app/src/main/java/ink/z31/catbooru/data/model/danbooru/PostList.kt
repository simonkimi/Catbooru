package ink.z31.catbooru.data.model.danbooru

import com.google.gson.annotations.SerializedName
import ink.z31.catbooru.data.model.base.BooruPost
import ink.z31.catbooru.data.model.base.RATING


fun getRating(name: String) = when (name) {
    "s" -> RATING.SAFE
    "q" -> RATING.QUESTIONABLE
    "e" -> RATING.EXPLICIT
    else -> RATING.QUESTIONABLE
}


data class DanbooruPost(
    val id: Int,
    @SerializedName("uploader_id")
    val creatorId: String,
    // 图片地址
    @SerializedName("file_url")
    val imgURL: String?,
    @SerializedName("preview_file_url")
    val previewURL: String?,
    @SerializedName("large_file_url")
    val sampleURL: String?,
    // 图片信息
    @SerializedName("image_width")
    val width: Int,
    @SerializedName("image_height")
    val height: Int,
    // 图片详情
    @SerializedName("rating")
    val rating: String,
    @SerializedName("is_status_locked")
    val status: Boolean,
    @SerializedName("tag_string")
    val tags: String,
    @SerializedName("source")
    val source: String
)


fun getDanbooruPostList(posts: List<DanbooruPost>) = posts.filter {
    !it.imgURL.isNullOrEmpty()
}.map {
    BooruPost(
        id = it.id,
        creatorId = it.creatorId,
        imgURL = it.imgURL!!,
        previewURL = it.previewURL!!,
        sampleURL = it.sampleURL!!,
        width = it.width,
        height = it.height,
        sampleHeight = it.height,
        sampleWidth = it.width,
        previewHeight = it.height,
        previewWidth = it.width,
        rating = getRating(it.rating),
        status = if (it.status) {
            "active"
        } else {
            "inactive"
        },
        tags = it.tags.split(" "),
        source = it.source
    )
}





