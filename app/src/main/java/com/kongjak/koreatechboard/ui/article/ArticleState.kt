package com.kongjak.koreatechboard.ui.article

import com.kongjak.koreatechboard.domain.model.Article
import com.kongjak.koreatechboard.util.routes.Department
import java.util.UUID

data class ArticleState(
    val isLoading: Boolean = false,
    val isLoaded: Boolean = false,
    val isSuccess: Boolean = false,
    val article: Article? = null,
    val uuid: UUID = UUID.randomUUID(),
    val department: String = Department.School.name,
    val statusCode: Int = 200,
    val url: String = "",
    val error: String = ""
)
