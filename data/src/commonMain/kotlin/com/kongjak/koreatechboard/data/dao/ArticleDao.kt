package com.kongjak.koreatechboard.data.dao

import com.kongjak.koreatechboard.data.enity.Article
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface ArticleDao {
    fun getAll(vararg departments: String): Flow<List<Article>>

    fun getArticle(uuid: Uuid): Article

    fun insert(article: Article)

    fun insertAll(vararg articles: Article)

    fun updateRead(uuid: Uuid, read: Boolean)

    fun delete(uuid: Uuid)

    fun deleteAll()
}
