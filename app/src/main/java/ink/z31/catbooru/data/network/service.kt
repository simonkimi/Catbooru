package ink.z31.catbooru.data.network

import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import ink.z31.catbooru.data.database.Booru
import ink.z31.catbooru.data.database.BooruType
import ink.z31.catbooru.data.model.base.BooruPost
import ink.z31.catbooru.data.network.interceptor.LoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder


class Service(booru: Booru) {
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(LoggingInterceptor())
        .build()

    private val tikXml: TikXml = TikXml.Builder()
        .exceptionOnUnreadXml(false)
        .build()

    private val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
        .create()

    private var retrofit: Retrofit

    init {
        retrofit = when (booru.type) {
            // XML为主的Booru
            // Gelbooru
            BooruType.GELBOORU.value -> Retrofit.Builder()
                .baseUrl(booru.url)
                .addConverterFactory(TikXmlConverterFactory.create(tikXml))
                .client(okHttpClient)
                .build()
            // JSON为主的Booru
            // Danbooru Moebooru
            BooruType.DANBOORU.value,
            BooruType.MOEBOORU.value -> Retrofit.Builder()
                .baseUrl(booru.url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            else -> Retrofit.Builder()
                .baseUrl(booru.url)
                .addConverterFactory(TikXmlConverterFactory.create(tikXml))
                .client(okHttpClient)
                .build()
        }
    }


    fun <T> creator(apiClass: Class<T>): T = retrofit.create(apiClass)

    inline fun <reified T> serviceCreator() = creator(T::class.java)
}

abstract class BooruNetwork {
    abstract suspend fun postsList(
        limit: Int,
        page: Int,
        tags: String
    ): List<BooruPost>


    abstract suspend fun tagList(
        limit: Int,
        orderBy: String,
        names: String
    )


    abstract suspend fun commentsList(
        postId: Int
    )
}