package ink.z31.catbooru.util

import android.util.Log
import okhttp3.*
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val TAG = "NetUtil"

object NetUtil {
    suspend fun get(url: String): ByteArray {
        return suspendCoroutine { continuation ->
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(url)
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    continuation.resume(ByteArray(0))
                }

                override fun onResponse(call: Call, response: Response) {
                    response.body()?.let {
                        continuation.resume(it.bytes())
                        return
                    }
                    continuation.resume(ByteArray(0))
                }
            })
        }
    }

    suspend fun getFavicon(host: String): String {
        val fav = get("${host}${if (host.endsWith("/")) "" else "/"}favicon.ico")
        return if (fav.isEmpty()) "" else String(Base64Util.b64Encode(fav))
    }
}