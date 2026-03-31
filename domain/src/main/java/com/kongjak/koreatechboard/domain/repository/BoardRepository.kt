package com.kongjak.koreatechboard.domain.repository

import androidx.paging.PagingData
import com.kongjak.koreatechboard.domain.model.Board
import com.kongjak.koreatechboard.domain.model.BoardData
import kotlinx.coroutines.flow.Flow

interface BoardRepository {
    fun getBoard(department: String, board: String): Flow<PagingData<BoardData>>
    suspend fun getBoardMinimum(department: String, board: String): Result<Board>
    fun searchTitle(department: String, board: String, title: String): Flow<PagingData<BoardData>>
}
