package com.kongjak.koreatechboard.data.mapper

import com.kongjak.koreatechboard.data.model.BoardResponse
import com.kongjak.koreatechboard.domain.model.Board

fun BoardResponse.mapToBoard(): Board {
    return BoardResponse(
        lastPage = this.lastPage,
        statusCode = this.statusCode,
        boardData = this.boardData
    )
}
