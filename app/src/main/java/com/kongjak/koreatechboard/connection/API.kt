package com.kongjak.koreatechboard.connection

import com.kongjak.koreatechboard.data.Article
import com.kongjak.koreatechboard.data.CseBoard
import com.kongjak.koreatechboard.data.DormBoard
import com.kongjak.koreatechboard.data.SchoolBoard
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface API {
    @GET("cse/{board}/")
    fun getCseBoard(
        @Path("board") board: String,
        @Query("page") page: Int
    ): Call<ArrayList<CseBoard>>

    @GET("cse/article/")
    fun getCseArticle(
        @Query("board") board: String,
        @Query("article") articleNum: Int
    ): Call<ArrayList<Article>>

    @GET("school/{board}/")
    fun getSchoolBoard(
        @Path("board") board: String,
        @Query("page") page: Int
    ): Call<ArrayList<SchoolBoard>>

    @GET("school/article/")
    fun getSchoolArticle(
        @Query("url") url: String
    ): Call<ArrayList<Article>>

    @GET("dorm/{board}/")
    fun getDormBoard(
        @Path("board") board: String,
        @Query("page") page: Int
    ): Call<ArrayList<DormBoard>>

    @GET("dorm/article/")
    fun getDormArticle(
        @Query("url") url: String
    ): Call<ArrayList<Article>>
}
