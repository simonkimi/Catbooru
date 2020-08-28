package ink.z31.catbooru.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    /**
     * 时间戳格式化
     */
    fun timeStamp2Date(timeStamp: String, format: String = "MM-dd HH:mm:ss"): String {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        return sdf.format(Date(timeStamp.toLong()))
    }


    /**
     * 取得当前时间戳（精确到毫秒）
     * @return
     */
    fun timeStamp(): String {
        return System.currentTimeMillis().toString()
    }
}