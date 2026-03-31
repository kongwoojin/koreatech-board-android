package com.kongjak.koreatechboard.domain.model

import java.util.UUID

data class LocalArticle(
    val uuid: UUID,
    val num: Int,
    val title: String,
    val writer: String,
    val content: String,
    val date: String,
    val articleUrl: String,
    val department: String,
    val board: String,
    val read: Boolean,
    val isNotice: Boolean,
    val receivedTime: Long
)
