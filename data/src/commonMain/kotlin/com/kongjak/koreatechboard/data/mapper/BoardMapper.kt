package com.kongjak.koreatechboard.data.mapper

import com.kongjak.koreatechboard.data.model.BoardResponse
import com.kongjak.koreatechboard.data.model.BoardResponseData
import com.kongjak.koreatechboard.domain.model.Board
import com.kongjak.koreatechboard.domain.model.BoardData

fun BoardResponse.mapToBoard(): Board {
    return Board(
        lastPage = this.lastPage,
        statusCode = this.statusCode,
        boardData = this.boardData?.map {
            it.mapToBoardData()
        } ?: emptyList()
    )
}

fun BoardResponseData.mapToBoardData(): BoardData = BoardData(
    uuid = this.uuid,
    title = this.title,
    num = this.num,
    writer = this.writer,
    writeDate = this.writeDate,
    read = this.read,
    isNew = this.isNew
)