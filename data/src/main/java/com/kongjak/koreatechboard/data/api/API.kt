package com.kongjak.koreatechboard.data.api

import com.kongjak.koreatechboard.data.model.ArticleResponse
import com.kongjak.koreatechboard.data.model.BoardResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface API {
    @GET(APIConstants.V3.BOARD)
    suspend fun getBoard(
        @Path("site") site: String,
        @Path("board") board: String,
        @Query("page") page: Int
    ): BoardResponse

    @GET(APIConstants.V3.BOARD_WIDGET)
    suspend fun getBoardMinimum(
        @Path("site") site: String,
        @Path("board") board: String
    ): BoardResponse

    @GET(APIConstants.V3.SEARCH_WITH_TITLE)
    suspend fun searchBoardWithTitle(
        @Path("site") site: String,
        @Path("board") board: String,
        @Query("title") title: String,
        @Query("page") page: Int = 1
    ): BoardResponse

    @GET(APIConstants.V3.ARTICLE)
    suspend fun getArticle(
        @Query("uuid") uuid: UUID
    ): ArticleResponse
}
