package com.kongjak.koreatechboard.domain.usecase.database

import com.kongjak.koreatechboard.domain.model.LocalArticle
import com.kongjak.koreatechboard.domain.repository.DatabaseRepository
import javax.inject.Inject

class InsertNewNoticeUseCase @Inject constructor(private val databaseRepository: DatabaseRepository) {
    suspend operator fun invoke(localArticle: LocalArticle) {
        databaseRepository.insertArticle(localArticle)
    }
}
