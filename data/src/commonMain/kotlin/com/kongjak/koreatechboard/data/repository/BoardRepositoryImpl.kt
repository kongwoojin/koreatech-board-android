package com.kongjak.koreatechboard.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kongjak.koreatechboard.data.api.API
import com.kongjak.koreatechboard.data.datasource.remote.BoardRemoteDataSource
import com.kongjak.koreatechboard.data.mapper.mapToBoard
import com.kongjak.koreatechboard.data.model.BoardResponse
import com.kongjak.koreatechboard.data.paging.BoardPagingSource
import com.kongjak.koreatechboard.data.paging.SearchTitlePagingSource
import com.kongjak.koreatechboard.domain.model.Board
import com.kongjak.koreatechboard.domain.model.BoardData
import com.kongjak.koreatechboard.domain.repository.BoardRepository
import io.ktor.client.call.body
import kotlinx.coroutines.flow.Flow

class BoardRepositoryImpl(
    private val boardRemoteDataSource: BoardRemoteDataSource,
    private val api: API
) :
    BoardRepository {
    override fun getBoard(department: String, board: String): Flow<PagingData<BoardData>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { BoardPagingSource(api, department, board) }
        ).flow
    }

    override suspend fun getBoardMinimum(department: String, board: String): Result<Board> {
        return try {
            Result.success(
                boardRemoteDataSource.getBoardMinimum(department, board).body<BoardResponse>()
                    .mapToBoard()
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun searchTitle(
        department: String,
        board: String,
        title: String
    ): Flow<PagingData<BoardData>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SearchTitlePagingSource(api, department, board, title) }
        ).flow
    }
}
