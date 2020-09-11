package ink.z31.catbooru.util

fun String.isUrl(): Boolean {
    return this.startsWith("http://") || this.startsWith("https://")
}