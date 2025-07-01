package com.kongjak.koreatechboard.domain.usecase.api

import com.benasher44.uuid.Uuid
import com.kongjak.koreatechboard.domain.model.Article
import com.kongjak.koreatechboard.domain.repository.ArticleRepository

class GetArticleUseCase(private val articleRepository: ArticleRepository) {
    suspend operator fun invoke(uuid: Uuid): Result<Article> {
        return articleRepository.getArticle(uuid)
    }
}
