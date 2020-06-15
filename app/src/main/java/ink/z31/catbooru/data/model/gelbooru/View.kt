package ink.z31.catbooru.data.model.gelbooru

import ink.z31.catbooru.data.model.base.BaseBooruView
import ink.z31.catbooru.data.model.base.BooruImage
import ink.z31.catbooru.data.model.base.BooruTag
import ink.z31.catbooru.data.model.base.Statistics
import org.jsoup.nodes.Element

class GelbooruView(html: String): BaseBooruView(html) {
    override fun parseTagList(): List<BooruTag> {
        val tagNodes = pageDocument.select("#tag-list>li")
        return tagNodes.map {
            val name = it.selectFirst("a:nth-child(4)")?.text() ?: "unknown"
            val number = it.selectFirst("span")?.text()?.toInt() ?: 0
            BooruTag(name, getTagType(it), number)
        }
    }

    override fun parseImage(): BooruImage {
        val image = pageDocument.selectFirst("#image")
        val resize = image.attr("src")
        val originalImageUrl = pageDocument
            .selectFirst("[href^=\"https://img\"]")
            ?.attr("href") ?: "unknown"
        return BooruImage(
            resize,
            originalImageUrl,
            image?.attr("data-original-width") ?: "unknown",
            image?.attr("data-original-height")  ?: "unknown"
        )
    }


    override fun parseStatistics(): Statistics {
        val sideBar = pageDocument.selectFirst("#tag-list div")
        val id: Element? = sideBar.selectFirst("li:contains(Id:)")
        val posted: Element? = sideBar.selectFirst("li:contains(Posted:)")
        val source: Element? = sideBar.selectFirst("li:contains(Source:) a")
        val rating: Element? = sideBar.selectFirst("li:contains(Rating:)")
        val score: Element? = sideBar.selectFirst("li:contains(Score:) span")
        return Statistics(
            id?.text() ?: "unknown",
            posted?.text() ?: "unknown",
            source?.attr("href") ?: "unknown",
            rating?.text() ?: "unknown",
            id?.text() ?: "unknown",
            score?.text() ?: "unknown"
        )
    }

}