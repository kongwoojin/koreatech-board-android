package com.kongjak.koreatechboard.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class APIImpl(private val httpClient: HttpClient) : API {
    override suspend fun getBoard(
        site: String,
        board: String,
        page: Int,
        numOfItems: Int
    ): HttpResponse {
        val response = httpClient.get {
            url("$site/$board")
            parameter("page", page)
            parameter("num_of_items", numOfItems)
        }

        return response.body()
    }

    override suspend fun getBoardMinimum(site: String, board: String): HttpResponse {
        val response = httpClient.get {
            url("$site/$board/widget")
        }

        return response.body()
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getArticle(uuid: Uuid): HttpResponse {
        val response = httpClient.get {
            url("article")
            parameter("uuid", uuid)
        }

        return response.body()
    }

    override suspend fun searchBoardWithTitle(
        site: String,
        board: String,
        title: String,
        page: Int
    ): HttpResponse {
        val response = httpClient.get {
            url("$site/$board/search/title")
            parameter("title", title)
            parameter("page", page)
        }

        return response.body()
    }
}
