package ink.z31.catbooru.data.model.base

import org.jsoup.Jsoup
import org.jsoup.nodes.Document


data class BooruPreviewImage(
    var id: Int,
    var imgSrc: String,
    var title: List<String>
)


interface IBooruList {
    fun getTagList(): List<BooruTag>
    fun getImageList(): List<BooruPreviewImage>
    fun getNextPage(): String
}


abstract class BaseBooruList(html: String) : IBooruList {
    protected val pageDocument: Document = Jsoup.parse(html)
    private var booruTagList: List<BooruTag>
    private var booruImgList: List<BooruPreviewImage>
    private var booruNextPage: String

    init {
        booruTagList = this.parseTag()
        booruImgList = this.parseImage()
        booruNextPage = this.parseFooter()
    }

    override fun getTagList(): List<BooruTag> {
        return this.booruTagList
    }

    override fun getImageList(): List<BooruPreviewImage> {
        return this.booruImgList
    }

    override fun getNextPage(): String {
        return this.booruNextPage
    }

    abstract fun parseTag(): List<BooruTag>
    abstract fun parseImage(): List<BooruPreviewImage>
    abstract fun parseFooter(): String
}