package com.kongjak.koreatechboard.data.model

import com.kongjak.koreatechboard.data.util.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
@OptIn(ExperimentalUuidApi::class)
data class ArticleResponse(
    @SerialName("status_code")
    val statusCode: Int,
    @Serializable(with = UUIDSerializer::class)
    @SerialName("id")
    val uuid: Uuid,
    @SerialName("num")
    val num: Int,
    @SerialName("title")
    val title: String,
    @SerialName("writer")
    val writer: String,
    @SerialName("content")
    val content: String,
    @SerialName("write_date")
    val date: String,
    @SerialName("article_url")
    val articleUrl: String,
    @SerialName("is_notice")
    val isNotice: Boolean,
    @SerialName("files")
    val files: List<FilesResponse>,
    @SerialName("error")
    val error: String
)
