package com.kongjak.koreatechboard.domain.usecase.api

import com.kongjak.koreatechboard.domain.model.Article
import com.kongjak.koreatechboard.domain.repository.ArticleRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class GetArticleUseCase(private val articleRepository: ArticleRepository) {
    @OptIn(ExperimentalUuidApi::class)
    suspend operator fun invoke(uuid: Uuid): Result<Article> {
        return articleRepository.getArticle(uuid)
    }
}
