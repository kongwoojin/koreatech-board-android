package com.kongjak.koreatechboard.domain.usecase.database

import com.kongjak.koreatechboard.domain.repository.DatabaseRepository
import java.util.UUID
import javax.inject.Inject

class UpdateNewNoticeReadUseCase @Inject constructor(private val databaseRepository: DatabaseRepository) {
    suspend operator fun invoke(uuid: UUID, read: Boolean) {
        databaseRepository.updateRead(uuid, read)
    }
}
