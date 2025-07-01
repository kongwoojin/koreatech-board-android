package com.kongjak.koreatechboard.domain.repository

import com.kongjak.koreatechboard.domain.model.LocalArticle
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface DatabaseRepository {
    suspend fun getArticleList(vararg departments: String): Flow<List<LocalArticle>>
    suspend fun getArticle(uuid: Uuid): LocalArticle
    suspend fun insertArticle(localArticle: LocalArticle)
    suspend fun insertArticleList(localArticleList: List<Uuid>, department: String, board: String, retryCount: Int = 0)
    suspend fun deleteArticle(uuid: Uuid)
    suspend fun deleteAllArticle()
    suspend fun updateRead(uuid: Uuid, read: Boolean)
}
