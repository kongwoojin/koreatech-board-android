package com.kongjak.koreatechboard.data.datasource.remote

import com.kongjak.koreatechboard.data.api.API
import com.kongjak.koreatechboard.data.model.ArticleResponse
import java.util.UUID
import javax.inject.Inject

class ArticleRemoteDataSource @Inject constructor(private val api: API) {
    suspend fun getArticle(uuid: UUID): ArticleResponse {
        return try {
            api.getArticle(uuid)
        } catch (e: Exception) {
            throw e
        }
    }
}
