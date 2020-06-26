package ink.z31.catbooru.data.network

import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import ink.z31.catbooru.data.database.Booru
import ink.z31.catbooru.data.model.base.BooruPost
import ink.z31.catbooru.data.model.gelbooru.GelbooruPost
import ink.z31.catbooru.data.network.interceptor.LoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

class Service(booru: Booru) {
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(LoggingInterceptor())
        .build()

    val tikXml = TikXml.Builder()
        .exceptionOnUnreadXml(false)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(booru.url)
        .addConverterFactory(TikXmlConverterFactory.create(tikXml))
        .client(okHttpClient)
        .build()

    fun <T> creator(apiClass: Class<T>): T = retrofit.create(apiClass)

    inline fun <reified T> serviceCreator() = creator(T::class.java)
}

abstract class BooruNetwork {
    abstract suspend fun postsList(
        limit: Int,
        pid: Int,
        tags: String
    ): List<BooruPost>


    abstract suspend fun tagList(
        limit: Int,
        orderBy: String,
        tags: String
    )


    abstract suspend fun commentsList(
        postId: Int
    )
}