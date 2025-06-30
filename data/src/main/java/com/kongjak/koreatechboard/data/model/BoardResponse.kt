package com.kongjak.koreatechboard.data.model

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class BoardResponse(
    @SerializedName("last_page")
    val lastPage: Int,
    @SerializedName("status_code")
    val statusCode: Int,
    @SerializedName("posts")
    val boardData: List<BoardResponseData>?
)

data class BoardResponseData(
    @SerializedName("id")
    val uuid: UUID,
    @SerializedName("title")
    val title: String,
    @SerializedName("num")
    val num: String,
    @SerializedName("writer")
    val writer: String,
    @SerializedName("write_date")
    val writeDate: String,
    @SerializedName("read_count")
    val read: Int,
    @SerializedName("is_new")
    val isNew: Boolean
)
