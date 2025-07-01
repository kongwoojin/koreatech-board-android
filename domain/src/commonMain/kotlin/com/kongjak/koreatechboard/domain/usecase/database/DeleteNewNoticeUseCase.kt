package com.kongjak.koreatechboard.domain.usecase.database

import com.kongjak.koreatechboard.domain.repository.DatabaseRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class DeleteNewNoticeUseCase(private val databaseRepository: DatabaseRepository) {
    @OptIn(ExperimentalUuidApi::class)
    suspend operator fun invoke(uuid: Uuid) {
        databaseRepository.deleteArticle(uuid)
    }
}
