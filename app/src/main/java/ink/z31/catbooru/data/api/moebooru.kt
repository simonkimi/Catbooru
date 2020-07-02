package ink.z31.catbooru.data.api

import ink.z31.catbooru.data.model.moebooru.MoebooruPost
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MoebooruAPI {
    @GET("/post.json")
    suspend fun postsList(
        @Query("limit") limit: Int = 50,
        @Query("page") page: Int = 1,
        @Query("tags") tags: String
    ): Response<List<MoebooruPost>>?

    @GET("tag.json")
    suspend fun tagList(
        @Query("limit") limit: Int = 50,
        @Query("order") orderBy: String,
        @Query("name") tags: String,
        @Query("page") page: String
    )

    @GET("/comment/show.json")
    suspend fun commentsList(
        @Query("id") postId: Int
    )
}