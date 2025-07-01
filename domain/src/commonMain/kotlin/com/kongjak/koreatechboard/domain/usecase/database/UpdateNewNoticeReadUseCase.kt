package com.kongjak.koreatechboard.domain.usecase.database

import com.kongjak.koreatechboard.domain.repository.DatabaseRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class UpdateNewNoticeReadUseCase(private val databaseRepository: DatabaseRepository) {
    @OptIn(ExperimentalUuidApi::class)
    suspend operator fun invoke(uuid: Uuid, read: Boolean) {
        databaseRepository.updateRead(uuid, read)
    }
}
