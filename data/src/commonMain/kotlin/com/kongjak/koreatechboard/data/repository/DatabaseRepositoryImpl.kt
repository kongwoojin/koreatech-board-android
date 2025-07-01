package com.kongjak.koreatechboard.data.repository

import com.kongjak.koreatechboard.data.datasource.local.DatabaseLocalDataSource
import com.kongjak.koreatechboard.data.datasource.remote.ArticleRemoteDataSource
import com.kongjak.koreatechboard.data.mapper.mapToArticle
import com.kongjak.koreatechboard.data.mapper.mapToLocalArticle
import com.kongjak.koreatechboard.data.model.ArticleResponse
import com.kongjak.koreatechboard.domain.model.LocalArticle
import com.kongjak.koreatechboard.domain.repository.DatabaseRepository
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.flow.flow
import java.io.IOException
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class DatabaseRepositoryImpl(
    private val databaseLocalDataSource: DatabaseLocalDataSource,
    private val articleRemoteDataSource: ArticleRemoteDataSource
) :
    DatabaseRepository {
    override suspend fun getArticleList(vararg departments: String) = flow {
        databaseLocalDataSource.getArticleList(*departments).collect {
            emit(it.mapToLocalArticle())
        }
    }

    override suspend fun getArticle(uuid: Uuid): LocalArticle {
        return databaseLocalDataSource.getArticle(uuid)
    }

    override suspend fun insertArticle(localArticle: LocalArticle) {
    }

    override suspend fun insertArticleList(
        localArticleList: List<Uuid>,
        department: String,
        board: String,
        retryCount: Int
    ) {
        localArticleList.map { uuid ->
            if (retryCount > 3) throw IOException("Failed to get article from API server.")
            try {
                val response = articleRemoteDataSource.getArticle(uuid)
                val data: ArticleResponse = response.body()
                data.mapToArticle(department, board).let {
                    databaseLocalDataSource.insertArticle(it)
                }
            } catch (e: HttpRequestTimeoutException) {
                insertArticleList(localArticleList, department, board, retryCount + 1)
            }
        }
    }

    override suspend fun deleteArticle(uuid: Uuid) {
        databaseLocalDataSource.deleteArticle(uuid)
    }

    override suspend fun deleteAllArticle() {
        databaseLocalDataSource.deleteAllArticle()
    }

    override suspend fun updateRead(uuid: Uuid, read: Boolean) {
        databaseLocalDataSource.updateRead(uuid, read)
    }
}
