package ink.z31.catbooru.util

object MathUtils {
    @JvmStatic
    fun clamp(x: Int, min: Int, max: Int): Int {
        if (x > max) return max
        return if (x < min) min else x
    }
}