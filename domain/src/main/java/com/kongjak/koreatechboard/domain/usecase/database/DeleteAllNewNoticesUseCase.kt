package com.kongjak.koreatechboard.domain.usecase.database

import com.kongjak.koreatechboard.domain.repository.DatabaseRepository
import javax.inject.Inject

class DeleteAllNewNoticesUseCase @Inject constructor(private val databaseRepository: DatabaseRepository) {
    suspend operator fun invoke() {
        databaseRepository.deleteAllArticle()
    }
}
