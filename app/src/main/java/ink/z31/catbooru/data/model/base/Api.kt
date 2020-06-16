package ink.z31.catbooru.data.model.base

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Path

interface BooruApi {
    suspend fun searchPost(tags: String, page: Int): Call<String>
}

inline fun <reified T> booruApiCreator(baseUrl: String): T = Retrofit
    .Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(ScalarsConverterFactory.create())
    .build()
    .create(T::class.java)
