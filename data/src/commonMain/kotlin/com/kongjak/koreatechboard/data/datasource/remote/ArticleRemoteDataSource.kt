package com.kongjak.koreatechboard.data.datasource.remote

import com.kongjak.koreatechboard.data.api.API
import io.ktor.client.statement.HttpResponse
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ArticleRemoteDataSource(private val api: API) {
    @OptIn(ExperimentalUuidApi::class)
    suspend fun getArticle(uuid: Uuid): HttpResponse {
        return api.getArticle(uuid)
    }
}
