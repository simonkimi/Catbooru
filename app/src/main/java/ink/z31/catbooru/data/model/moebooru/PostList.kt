package ink.z31.catbooru.data.model.moebooru

import com.google.gson.annotations.SerializedName
import com.tickaroo.tikxml.annotation.Attribute
import ink.z31.catbooru.data.model.base.BooruPost
import ink.z31.catbooru.data.model.danbooru.DanbooruPost
import ink.z31.catbooru.data.model.danbooru.getRating

data class MoebooruPost(
    val id: Int,
    @SerializedName("creator_id")
    val creatorId: String,
    // 图片地址
    @SerializedName("file_url")
    val imgURL: String,
    @SerializedName("preview_url")
    val previewURL: String,
    @SerializedName("sample_url")
    val sampleURL: String,
    // 图片信息
    @SerializedName("width")
    val width: Int,
    @SerializedName("height")
    val height: Int,
    @SerializedName("sample_width")
    val sampleWidth: Int,
    @SerializedName("sample_height")
    val sampleHeight: Int,
    @SerializedName("preview_width")
    val previewWidth: Int,
    @SerializedName("preview_height")
    val previewHeight: Int,
    // 图片详情
    @SerializedName("rating")
    val rating: String,
    @SerializedName("is_status_locked")
    val status: Boolean,
    @SerializedName("tags")
    val tags: String,
    @SerializedName("source")
    val source: String
)

fun getMoebooruPostList(posts: List<MoebooruPost>) = posts.map {
    BooruPost(
        id = it.id,
        creatorId = it.creatorId,
        imgURL = it.imgURL,
        previewURL = it.previewURL,
        sampleURL = it.sampleURL,
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
