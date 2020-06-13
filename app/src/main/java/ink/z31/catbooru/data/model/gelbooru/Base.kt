package ink.z31.catbooru.data.model.gelbooru

import ink.z31.catbooru.data.model.base.BooruTAGType
import org.jsoup.nodes.Element

fun getTagType(ele: Element) = when {
    ele.hasClass("tag-type-general") -> BooruTAGType.GENERAL
    ele.hasClass("tag-type-artist") -> BooruTAGType.ARTIST
    ele.hasClass("tag-type-character") -> BooruTAGType.CHARACTER
    ele.hasClass("tag-type-copyright") -> BooruTAGType.COPYRIGHT
    ele.hasClass("tag-type-metadata") -> BooruTAGType.METADATA
    else -> BooruTAGType.GENERAL
}