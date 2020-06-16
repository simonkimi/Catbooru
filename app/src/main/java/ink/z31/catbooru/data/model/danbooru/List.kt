package ink.z31.catbooru.data.model.danbooru

import ink.z31.catbooru.data.model.base.BaseBooruList
import ink.z31.catbooru.data.model.base.BooruPreviewImage
import ink.z31.catbooru.data.model.base.BooruTag

class DanbooruSearchPage(html: String) : BaseBooruList(html) {
    override fun parseTag(): List<BooruTag> {
        TODO("Not yet implemented")
    }

    override fun parseImage(): List<BooruPreviewImage> {
        TODO("Not yet implemented")
    }

    override fun parseFooter(): String {
        TODO("Not yet implemented")
    }
}