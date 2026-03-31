package com.kongjak.koreatechboard.data.repository

import com.kongjak.koreatechboard.data.datasource.remote.ArticleRemoteDataSource
import com.kongjak.koreatechboard.data.mapper.mapToArticle
import com.kongjak.koreatechboard.domain.model.Article
import com.kongjak.koreatechboard.domain.repository.ArticleRepository
import java.util.UUID
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(private val articleRemoteDataSource: ArticleRemoteDataSource) :
    ArticleRepository {
    override suspend fun getArticle(uuid: UUID): Result<Article> {
        return try {
            val response = articleRemoteDataSource.getArticle(uuid)
            Result.success(response.mapToArticle())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
