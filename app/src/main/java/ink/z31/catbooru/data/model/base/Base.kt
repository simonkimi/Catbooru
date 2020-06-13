package ink.z31.catbooru.data.model.base

data class BooruTag(
    var name: String,
    var type: BooruTAGType,
    var number: Int
)

enum class BooruTAGType {
    GENERAL,
    COPYRIGHT,
    ARTIST,
    CHARACTER,
    METADATA
}

data class BooruImage (
    var resizeImageUrl: String,
    var originalImageUrl: String,
    var originalWidth: String,
    var originalHeight: String
)