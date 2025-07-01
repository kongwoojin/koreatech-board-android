package com.kongjak.koreatechboard.ui.article

import com.kongjak.koreatechboard.domain.model.Article
import com.kongjak.koreatechboard.util.routes.Department
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class ArticleState @OptIn(ExperimentalUuidApi::class) constructor(
    val isLoading: Boolean = false,
    val isLoaded: Boolean = false,
    val isSuccess: Boolean = false,
    val article: Article? = null,
    val uuid: Uuid = Uuid.random(),
    val department: String = Department.School.name,
    val statusCode: Int = 200,
    val url: String = "",
    val error: String = ""
)
