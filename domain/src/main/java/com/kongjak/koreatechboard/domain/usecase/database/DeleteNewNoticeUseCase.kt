package com.kongjak.koreatechboard.domain.usecase.database

import com.kongjak.koreatechboard.domain.repository.DatabaseRepository
import java.util.UUID
import javax.inject.Inject

class DeleteNewNoticeUseCase @Inject constructor(private val databaseRepository: DatabaseRepository) {
    suspend operator fun invoke(uuid: UUID) {
        databaseRepository.deleteArticle(uuid)
    }
}
