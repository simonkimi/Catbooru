package ink.z31.catbooru.data.network

import ink.z31.catbooru.data.api.GelbooruAPI
import ink.z31.catbooru.data.api.MoebooruAPI
import ink.z31.catbooru.data.database.Booru
import ink.z31.catbooru.data.model.base.BooruPost
import ink.z31.catbooru.data.model.moebooru.MoebooruPost
import ink.z31.catbooru.data.model.moebooru.getMoebooruPostList

class MoebooruNetwork(booru: Booru) : BooruNetwork() {
    private val booruService = Service(booru).serviceCreator<MoebooruAPI>()

    override suspend fun postsList(limit: Int, page: Int, tags: String): List<BooruPost> {
        val data = booruService.postsList(limit, page, tags)?.body() ?: listOf()
        return getMoebooruPostList(data)
    }

    override suspend fun tagList(limit: Int, orderBy: String, names: String) {
        TODO("Not yet implemented")
    }

    override suspend fun commentsList(postId: Int) {
        TODO("Not yet implemented")
    }
}