package ink.z31.catbooru.data.model.base



enum class RATING {
    SAFE, EXPLICIT, QUESTIONABLE
}



data class BooruPost(
    // 基础信息
    val id: Int,
    val creatorId: Int,
    // 图片地址
    val imgURL: String,
    val previewURL: String,
    val sampleURL: String,
    // 图片信息
    val width: Int,
    val height: Int,
    val sampleWidth: Int,
    val sampleHeight: Int,
    val previewWidth: Int,
    val previewHeight: Int,
    // 图片详情
    val rating: RATING,
    val status: String,
    val tags: List<String>,
    val source: String
)