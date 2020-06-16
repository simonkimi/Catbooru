package ink.z31.catbooru.data.model.danbooru


import ink.z31.catbooru.data.model.base.BooruApi
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DanbooruServer : BooruApi {
    @GET("posts?page={page}&tags={tags}")
    override suspend fun searchPost(
        @Path("tags") tags: String,
        @Path("page") page: Int
    ): Call<String>

}