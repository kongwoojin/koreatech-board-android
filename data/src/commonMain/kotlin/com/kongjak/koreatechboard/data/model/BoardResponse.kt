package com.kongjak.koreatechboard.data.model

import com.benasher44.uuid.Uuid
import com.kongjak.koreatechboard.data.util.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BoardResponse(
    @SerialName("last_page")
    val lastPage: Int,
    @SerialName("status_code")
    val statusCode: Int,
    @SerialName("posts")
    val boardData: List<BoardResponseData>?,
    @SerialName("error")
    val error: String
)

@Serializable
data class BoardResponseData(
    @Serializable(with = UUIDSerializer::class)
    @SerialName("id")
    val uuid: Uuid,
    @SerialName("title")
    val title: String,
    @SerialName("num")
    val num: Int,
    @SerialName("writer")
    val writer: String,
    @SerialName("write_date")
    val writeDate: String,
    @SerialName("read_count")
    val read: Int,
    @SerialName("is_new")
    val isNew: Boolean,
    @SerialName("is_notice")
    val isNotice: Boolean
)
