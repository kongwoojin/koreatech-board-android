package com.kongjak.koreatechboard.domain.usecase.api

import com.kongjak.koreatechboard.domain.model.Board
import com.kongjak.koreatechboard.domain.repository.BoardRepository

class GetBoardMinimumUseCase(private val boardRepository: BoardRepository) {
    suspend operator fun invoke(department: String, board: String): Result<Board> {
        return boardRepository.getBoardMinimum(department, board)
    }
}
