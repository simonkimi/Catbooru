package ink.z31.catbooru.data.model.gelbooru

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml
import ink.z31.catbooru.data.model.base.BooruPost
import ink.z31.catbooru.data.model.base.BooruPostEnd
import ink.z31.catbooru.data.model.base.RATING

fun getRating(name: String) = when (name) {
    "s" -> RATING.SAFE
    "q" -> RATING.QUESTIONABLE
    "e" -> RATING.EXPLICIT
    else -> RATING.QUESTIONABLE
}

@Xml(name = "post")
data class GelbooruPost(
    @Attribute(name = "id")
    val id: Int,
    @Attribute(name = "creator_id")
    val creatorId: String,
    // 图片地址
    @Attribute(name = "file_url")
    val imgURL: String,
    @Attribute(name = "preview_url")
    val previewURL: String,
    @Attribute(name = "sample_url")
    val sampleURL: String,
    // 图片信息
    @Attribute(name = "width")
    val width: Int,
    @Attribute(name = "height")
    val height: Int,
    @Attribute(name = "sample_width")
    val sampleWidth: Int,
    @Attribute(name = "sample_height")
    val sampleHeight: Int,
    @Attribute(name = "preview_width")
    val previewWidth: Int,
    @Attribute(name = "preview_height")
    val previewHeight: Int,
    // 图片详情
    @Attribute(name = "rating")
    val rating: String,
    @Attribute(name = "status")
    val status: String,
    @Attribute(name = "tags")
    val tags: String,
    @Attribute(name = "source")
    val source: String
)

@Xml(name = "posts")
data class GelbooruPostList(
    @Attribute
    val offset: Int,
    @Attribute
    val count: Int,
    @Element
    var posts: List<GelbooruPost>?
) {
    fun getBooruList(): List<BooruPost> {
        if (posts != null) {
            return posts?.map {
                BooruPost(
                    id = it.id,
                    creatorId = it.creatorId,
                    imgURL = it.imgURL,
                    previewURL = it.previewURL,
                    sampleURL = it.sampleURL,
                    width = it.width,
                    height = it.height,
                    sampleHeight = it.sampleHeight,
                    sampleWidth = it.sampleWidth,
                    previewHeight = it.previewHeight,
                    previewWidth = it.previewWidth,
                    rating = getRating(it.rating),
                    status = it.status,
                    tags = it.tags.split(" "),
                    source = it.source
                )
            } ?: mutableListOf()
        } else {
            throw BooruPostEnd("加载完成")
        }
    }
}


