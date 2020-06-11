package ink.z31.catbooru.data.model.base

import org.jsoup.Jsoup
import org.jsoup.nodes.Document


data class BooruTag(
    var name: String,
    var type: BooruTAGType,
    var number: Int
)


data class BooruImage(
    var id: Int,
    var imgSrc: String,
    var title: List<String>
)


enum class BooruTAGType {
    GENERAL,
    COPYRIGHT,
    ARTIST,
    CHARACTER,
    METADATA
}

interface IBaseBooruSearchPage {
    fun getTagList(): List<BooruTag>
    fun getImageList(): List<BooruImage>
}


abstract class BaseBooruSearchPage(html: String) : IBaseBooruSearchPage {
    protected val pageDocument: Document = Jsoup.parse(html)
    private var booruTagList: List<BooruTag>
    private var booruImgList: List<BooruImage>

    init {
        booruTagList = this.parseTag()
        booruImgList = this.parseImage()
    }

    override fun getTagList(): List<BooruTag> {
        return this.booruTagList
    }

    override fun getImageList(): List<BooruImage> {
        return this.booruImgList
    }

    abstract fun parseTag(): List<BooruTag>
    abstract fun parseImage(): List<BooruImage>
}