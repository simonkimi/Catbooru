package ink.z31.catbooru.data.model.gelbooru

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml
import ink.z31.catbooru.data.model.base.BooruTAGType
import ink.z31.catbooru.data.model.base.BooruTag


@Xml(name = "tags")
data class TagList(
    @Element
    val tags: List<Tag>
) {
    fun getTagList() = tags.map {
        BooruTag(
            name = it.name,
            count = it.count,
            type = getTagType(it.type)
        )
    }
}


@Xml(name = "tag")
data class Tag(
    @Attribute
    val type: Int,
    @Attribute
    val count: Int,
    @Attribute
    val name: String,
    @Attribute
    val id: Int
)

fun getTagType(type: Int) = when(type) {
    0 -> BooruTAGType.GENERAL
    1 -> BooruTAGType.ARTIST
    3 -> BooruTAGType.COPYRIGHT
    5 -> BooruTAGType.METADATA
    4 -> BooruTAGType.CHARACTER
    else -> BooruTAGType.GENERAL
}