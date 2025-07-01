package com.kongjak.koreatechboard.domain.model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class Board(
    val lastPage: Int,
    val statusCode: Int,
    val boardData: List<BoardData>?
)

data class BoardData @OptIn(ExperimentalUuidApi::class) constructor(
    val uuid: Uuid,
    val title: String,
    val num: Int,
    val writer: String,
    val writeDate: String,
    val read: Int,
    val isNew: Boolean
)
