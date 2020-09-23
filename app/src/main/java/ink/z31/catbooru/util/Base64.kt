package ink.z31.catbooru.util

import android.util.Base64

object Base64Util {
    fun b64Encode(input: ByteArray): ByteArray {
        return Base64.encode(input, Base64.DEFAULT)
    }

    fun b64Decode(input: ByteArray): ByteArray {
        return Base64.decode(input, Base64.DEFAULT)
    }
}
