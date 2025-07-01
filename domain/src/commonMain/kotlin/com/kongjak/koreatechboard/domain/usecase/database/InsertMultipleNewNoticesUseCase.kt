package com.kongjak.koreatechboard.domain.usecase.database

import com.kongjak.koreatechboard.domain.repository.DatabaseRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class InsertMultipleNewNoticesUseCase(private val databaseRepository: DatabaseRepository) {
    @OptIn(ExperimentalUuidApi::class)
    suspend operator fun invoke(localArticleList: List<Uuid>, department: String, board: String) {
        databaseRepository.insertArticleList(localArticleList, department, board)
    }
}
