package com.kongjak.koreatechboard.data.datasource.remote

import com.kongjak.koreatechboard.data.api.API
import com.kongjak.koreatechboard.data.model.ArticleResponse
import javax.inject.Inject

class ArticleRemoteDataSource @Inject constructor(private val api: API) {
    suspend fun getArticle(site: String, url: String): ArticleResponse {
        return api.getArticle(site, url)
    }
}