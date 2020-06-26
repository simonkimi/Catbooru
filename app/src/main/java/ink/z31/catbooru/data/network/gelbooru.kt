package ink.z31.catbooru.data.network

import ink.z31.catbooru.data.api.GelbooruAPI
import ink.z31.catbooru.data.database.Booru
import ink.z31.catbooru.data.model.base.BooruPost
import ink.z31.catbooru.data.model.base.RATING
import ink.z31.catbooru.data.model.gelbooru.GelbooruPost
import retrofit2.Response





class GelbooruNetwork(booru: Booru) : BooruNetwork() {
    private val gelbooruService = Service(booru).serviceCreator<GelbooruAPI>()


    override suspend fun postsList(limit: Int, pid: Int, tags: String): List<BooruPost>
            = gelbooruService.postsList(limit, pid, tags).body()?.getBooruList() ?: listOf()

    override suspend fun tagList(limit: Int, orderBy: String, tags: String) {
        TODO("Not yet implemented")
    }

    override suspend fun commentsList(postId: Int) {
        TODO("Not yet implemented")
    }
}
