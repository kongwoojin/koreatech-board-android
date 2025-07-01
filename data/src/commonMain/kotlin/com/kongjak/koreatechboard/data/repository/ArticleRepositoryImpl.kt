package com.kongjak.koreatechboard.data.repository

import com.kongjak.koreatechboard.data.datasource.remote.ArticleRemoteDataSource
import com.kongjak.koreatechboard.data.mapper.mapToArticle
import com.kongjak.koreatechboard.data.model.ArticleResponse
import com.kongjak.koreatechboard.domain.model.Article
import com.kongjak.koreatechboard.domain.repository.ArticleRepository
import io.ktor.client.call.body
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ArticleRepositoryImpl(private val articleRemoteDataSource: ArticleRemoteDataSource) :
    ArticleRepository {
    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getArticle(uuid: Uuid): Result<Article> {
        return try {
            Result.success(
                articleRemoteDataSource.getArticle(uuid).body<ArticleResponse>().mapToArticle()
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
