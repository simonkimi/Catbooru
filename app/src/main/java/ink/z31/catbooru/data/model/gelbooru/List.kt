package ink.z31.catbooru.data.model.gelbooru

import ink.z31.catbooru.data.model.base.BaseBooruList
import ink.z31.catbooru.data.model.base.BooruPreviewImage
import ink.z31.catbooru.data.model.base.BooruTag

class GelbooruSearchPage(html: String) : BaseBooruList(html) {

    override fun parseTag(): List<BooruTag> {
        val tagNodes = pageDocument.select("#searchTags>li")
        return tagNodes.map {
            val name = it.selectFirst("a:nth-child(4)")?.text() ?: "None"
            val number = it.selectFirst("span")?.text()?.toInt() ?: 0
            BooruTag(name, getTagType(it), number)
        }
    }

    override fun parseImage(): List<BooruPreviewImage> {
        val imgNode = pageDocument.select(".thumbnail-preview img")
        return imgNode.map { node ->
            val id = Regex("\\d+").find(node.attr("alt"))?.value?.toInt() ?: 0
            val src = node.attr("src")
            val title = node.attr("title")
                .split(" ")
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .map { it.replace(" ", "_") }
            BooruPreviewImage(id, src, title)
        }
    }

    override fun parseFooter(): String {
        val a = pageDocument.selectFirst("[alt=\"next\"]")
        return a.attr("src")
    }

}

