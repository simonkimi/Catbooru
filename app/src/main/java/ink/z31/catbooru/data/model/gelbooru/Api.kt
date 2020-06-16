package ink.z31.catbooru.data.model.gelbooru

import ink.z31.catbooru.data.model.base.BooruApi
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GelbooruServer : BooruApi {
    @GET("index.php?page=post&s=list&tags={tags}&pid={page}")
    override suspend fun searchPost(
        @Path("tags") tags: String,
        @Path("page") page: Int
    ): Call<String>

}