package ink.z31.catbooru.data.api

import ink.z31.catbooru.data.model.base.BooruPost
import ink.z31.catbooru.data.model.gelbooru.GelbooruPost
import ink.z31.catbooru.data.model.gelbooru.GelbooruPostList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GelbooruAPI {
    @GET("/index.php?page=dapi&s=post&q=index")
    suspend fun postsList(
        @Query("limit") limit: Int,
        @Query("pid") pid: Int,
        @Query("tags") tags: String
    ): Response<GelbooruPostList>

    @GET("/index.php?page=dapi&s=tag&q=index")
    suspend fun tagList(
        @Query("limit") limit: Int,
        @Query("orderby") orderBy: String,
        @Query("tags") tags: String
    )

    @GET("/index.php?page=dapi&s=comment&q=index")
    suspend fun commentsList(
        @Query("post_id") postId: Int
    )
}