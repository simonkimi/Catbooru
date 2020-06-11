package ink.z31.catbooru.data.model.gelbooru

import ink.z31.catbooru.data.model.base.BaseBooruSearchPage
import ink.z31.catbooru.data.model.base.BooruImage
import ink.z31.catbooru.data.model.base.BooruTAGType
import ink.z31.catbooru.data.model.base.BooruTag
import org.jsoup.nodes.Element

class BooruPage(html: String) : BaseBooruSearchPage(html) {

    override fun parseTag(): List<BooruTag> {
        val tagNodes = pageDocument.select("#searchTags>li")
        return tagNodes.map {
            val name = it.selectFirst("a:nth-child(4)").text()
            val number = it.selectFirst("span").text().toInt()
            BooruTag(name, getTagType(it), number)
        }
    }

    override fun parseImage(): List<BooruImage> {
        val imgNode = pageDocument.select(".thumbnail-preview img")
        return imgNode.map { node ->
            val id = Regex("\\d+").find(node.attr("alt"))?.value?.toInt() ?: 0
            val src = node.attr("src")
            val title = node.attr("title")
                .split(" ")
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .map { it.replace(" ", "_") }
            BooruImage(id, src, title)
        }
    }


    private fun getTagType(ele: Element) = when {
        ele.hasClass("tag-type-general") -> BooruTAGType.GENERAL
        ele.hasClass("tag-type-artist") -> BooruTAGType.ARTIST
        ele.hasClass("tag-type-character") -> BooruTAGType.CHARACTER
        ele.hasClass("tag-type-copyright") -> BooruTAGType.COPYRIGHT
        ele.hasClass("tag-type-metadata") -> BooruTAGType.METADATA
        else -> BooruTAGType.GENERAL
    }
}