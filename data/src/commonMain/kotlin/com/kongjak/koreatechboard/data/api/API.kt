package com.kongjak.koreatechboard.data.api

import io.ktor.client.statement.HttpResponse
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface API {
    suspend fun getBoard(
        site: String,
        board: String,
        page: Int = 1,
        numOfItems: Int = 20
    ): HttpResponse

    suspend fun getBoardMinimum(
        site: String,
        board: String
    ): HttpResponse

    @OptIn(ExperimentalUuidApi::class)
    suspend fun getArticle(
        uuid: Uuid
    ): HttpResponse

    suspend fun searchBoardWithTitle(
        site: String,
        board: String,
        title: String,
        page: Int = 1
    ): HttpResponse
}
