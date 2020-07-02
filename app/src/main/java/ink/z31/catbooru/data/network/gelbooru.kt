package ink.z31.catbooru.data.network

import ink.z31.catbooru.data.api.GelbooruAPI
import ink.z31.catbooru.data.database.Booru
import ink.z31.catbooru.data.model.base.BooruPost





class GelbooruNetwork(booru: Booru) : BooruNetwork() {
    private val gelbooruService = Service(booru).serviceCreator<GelbooruAPI>()

    override suspend fun postsList(limit: Int, page: Int, tags: String): List<BooruPost>
            = gelbooruService.postsList(limit, page, tags)?.body()?.getBooruList() ?: listOf()

    override suspend fun tagList(limit: Int, orderBy: String, names: String) {
        TODO("Not yet implemented")
    }

    override suspend fun commentsList(postId: Int) {
        TODO("Not yet implemented")
    }
}
