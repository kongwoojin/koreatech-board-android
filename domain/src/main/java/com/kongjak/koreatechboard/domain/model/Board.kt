package com.kongjak.koreatechboard.domain.model

import java.util.UUID

data class Board(
    val lastPage: Int,
    val statusCode: Int,
    val boardData: List<BoardData>?
)

data class BoardData(
    val uuid: UUID,
    val title: String,
    val num: String,
    val writer: String,
    val writeDate: String,
    val read: Int,
    val isNew: Boolean
)
