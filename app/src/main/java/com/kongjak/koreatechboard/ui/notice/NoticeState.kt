package com.kongjak.koreatechboard.ui.notice

import com.kongjak.koreatechboard.domain.model.LocalArticle

data class NoticeState(
    val isLoaded: Boolean = false,
    val articles: List<LocalArticle> = emptyList(),
    val selectedDepartment: List<Int> = listOf(0, 1, 2),
    val userDepartment: Int = 0
)
