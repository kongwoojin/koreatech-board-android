package com.kongjak.koreatechboard.domain.usecase.database

import com.kongjak.koreatechboard.domain.repository.DatabaseRepository
import java.util.UUID
import javax.inject.Inject

class InsertMultipleNewNoticesUseCase @Inject constructor(private val databaseRepository: DatabaseRepository) {
    suspend operator fun invoke(localArticleList: List<UUID>, department: String, board: String) {
        databaseRepository.insertArticleList(localArticleList, department, board)
    }
}
