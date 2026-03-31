package com.kongjak.koreatechboard.domain.repository

import com.kongjak.koreatechboard.domain.model.LocalArticle
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface DatabaseRepository {
    suspend fun getArticleList(vararg departments: String): Flow<List<LocalArticle>>
    suspend fun getArticle(uuid: UUID): LocalArticle
    suspend fun insertArticle(localArticle: LocalArticle)
    suspend fun insertArticleList(localArticleList: List<UUID>, department: String, board: String, retryCount: Int = 0)
    suspend fun deleteArticle(uuid: UUID)
    suspend fun deleteAllArticle()
    suspend fun updateRead(uuid: UUID, read: Boolean)
}
