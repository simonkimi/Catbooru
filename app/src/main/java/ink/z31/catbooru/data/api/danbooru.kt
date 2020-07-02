package ink.z31.catbooru.data.api

import ink.z31.catbooru.data.model.danbooru.DanbooruPost
import ink.z31.catbooru.data.model.gelbooru.GelbooruPostList
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DanbooruAPI {
    @GET("posts.json")
    suspend fun postsList(
        @Query("limit") limit: Int = 50,
        @Query("page") page: Int = 1,
        @Query("tags") tags: String
    ): Response<List<DanbooruPost>>

    @GET("tags.json")
    suspend fun tagList(
        @Query("limit") limit: Int = 50,
        @Query("order") orderBy: String,
        @Query("search[name_comma]") tags: String,
        @Query("page") page: String
    )

    @GET("comments.json")
    suspend fun commentsList(
        @Query("search[post_id]") postId: Int
    )
}