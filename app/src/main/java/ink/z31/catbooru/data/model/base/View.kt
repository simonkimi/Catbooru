package ink.z31.catbooru.data.model.base

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

data class Statistics(
    var id: String,
    var posted: String,
    var by: String,
    var size: String,
    var rating: String,
    var score: String
)


interface IBooruView {
    fun getTagList(): List<BooruTag>
    fun getImage(): BooruImage
    fun getStatistics(): Statistics
}


abstract class BaseBooruView(html: String) : IBooruView {
    protected val pageDocument: Document = Jsoup.parse(html)
    private var tagList: List<BooruTag>
    private var image: BooruImage
    private var statistics: Statistics

    init {
        tagList = this.parseTagList()
        image = this.parseImage()
        statistics = this.parseStatistics()
    }

    abstract fun parseTagList(): List<BooruTag>
    abstract fun parseImage(): BooruImage
    abstract fun parseStatistics(): Statistics


    override fun getImage(): BooruImage {
        return image
    }

    override fun getTagList(): List<BooruTag> {
        return tagList
    }


    override fun getStatistics(): Statistics {
        return statistics
    }
}