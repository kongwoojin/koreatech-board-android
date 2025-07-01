package com.kongjak.koreatechboard.domain.repository

import com.kongjak.koreatechboard.domain.model.Article
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface ArticleRepository {
    @OptIn(ExperimentalUuidApi::class)
    suspend fun getArticle(uuid: Uuid): Result<Article>
}
