package com.kongjak.koreatechboard.domain.usecase.database

import com.kongjak.koreatechboard.domain.model.LocalArticle
import com.kongjak.koreatechboard.domain.repository.DatabaseRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetAllNewNoticesUseCase @Inject constructor(private val databaseRepository: DatabaseRepository) {
    suspend operator fun invoke(vararg departments: String): Flow<List<LocalArticle>> {
        return databaseRepository.getArticleList(*departments)
    }
}
