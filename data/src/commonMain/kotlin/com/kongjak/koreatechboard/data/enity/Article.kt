package com.kongjak.koreatechboard.data.enity

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class Article @OptIn(ExperimentalUuidApi::class) constructor(
    val uuid: Uuid,
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
