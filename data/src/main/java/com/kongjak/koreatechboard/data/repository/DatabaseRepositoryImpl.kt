package com.kongjak.koreatechboard.data.repository

import com.kongjak.koreatechboard.data.datasource.local.DatabaseLocalDataSource
import com.kongjak.koreatechboard.data.datasource.remote.ArticleRemoteDataSource
import com.kongjak.koreatechboard.data.mapper.mapToArticle
import com.kongjak.koreatechboard.data.mapper.mapToLocalArticle
import com.kongjak.koreatechboard.domain.model.LocalArticle
import com.kongjak.koreatechboard.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

class DatabaseRepositoryImpl @Inject constructor(
    private val databaseLocalDataSource: DatabaseLocalDataSource,
    private val articleRemoteDataSource: ArticleRemoteDataSource
) : DatabaseRepository {
    override suspend fun getArticleList(vararg departments: String): Flow<List<LocalArticle>> =
        databaseLocalDataSource.getArticleList(*departments).map { list ->
            list.map { it.mapToLocalArticle() }
        }

    override suspend fun getArticle(uuid: UUID): LocalArticle {
        return databaseLocalDataSource.getArticle(uuid)
    }

    override suspend fun insertArticle(localArticle: LocalArticle) {
    }

    override suspend fun insertArticleList(
        localArticleList: List<UUID>,
        department: String,
        board: String,
        retryCount: Int
    ) {
        localArticleList.map { uuid ->
            if (retryCount > 3) throw IOException("Failed to get article from API server.")
            try {
                val response = articleRemoteDataSource.getArticle(uuid)
                response.mapToArticle(department, board).let {
                    databaseLocalDataSource.insertArticle(it)
                }
            } catch (e: IOException) {
                insertArticleList(localArticleList, department, board, retryCount + 1)
            }
        }
    }

    override suspend fun deleteArticle(uuid: UUID) {
        databaseLocalDataSource.deleteArticle(uuid)
    }

    override suspend fun deleteAllArticle() {
        databaseLocalDataSource.deleteAllArticle()
    }

    override suspend fun updateRead(uuid: UUID, read: Boolean) {
        databaseLocalDataSource.updateRead(uuid, read)
    }
}
