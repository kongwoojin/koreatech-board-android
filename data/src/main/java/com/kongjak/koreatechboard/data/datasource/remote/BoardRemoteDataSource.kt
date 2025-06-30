package com.kongjak.koreatechboard.data.datasource.remote

import com.kongjak.koreatechboard.data.api.API
import com.kongjak.koreatechboard.data.model.BoardResponse
import javax.inject.Inject

class BoardRemoteDataSource @Inject constructor(private val api: API) {
    suspend fun getBoardMinimum(department: String, board: String): BoardResponse {
        return try {
            api.getBoardMinimum(department, board)
        } catch (e: Exception) {
            throw e
        }
    }
}
