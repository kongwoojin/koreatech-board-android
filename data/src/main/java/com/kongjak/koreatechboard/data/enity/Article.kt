package com.kongjak.koreatechboard.data.enity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Article(
    @PrimaryKey val uuid: UUID,
    @ColumnInfo("num")
    val num: Int,
    @ColumnInfo("title")
    val title: String,
    @ColumnInfo("writer")
    val writer: String,
    @ColumnInfo("content")
    val content: String,
    @ColumnInfo("write_date")
    val date: String,
    @ColumnInfo("article_url")
    val articleUrl: String,
    @ColumnInfo("department")
    val department: String,
    @ColumnInfo("board")
    val board: String,
    @ColumnInfo("read")
    val read: Boolean,
    @ColumnInfo("is_notice")
    val isNotice: Boolean,
    @ColumnInfo("received_time")
    val receivedTime: Long
)
