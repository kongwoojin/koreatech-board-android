package com.kongjak.koreatechboard.data.mapper

import com.kongjak.koreatechboard.data.enity.Article
import com.kongjak.koreatechboard.data.model.ArticleResponse
import com.kongjak.koreatechboard.domain.model.LocalArticle
import java.util.concurrent.TimeUnit

fun List<Article>.mapToLocalArticle(): List<LocalArticle> {
    return this.map {
        LocalArticle(
            uuid = it.uuid,
            num = it.num,
            title = it.title,
            writer = it.writer,
            content = it.content,
            date = it.date,
            articleUrl = it.articleUrl,
            department = it.department,
            board = it.board,
            read = it.read,
            isNotice = it.isNotice,
            receivedTime = it.receivedTime
        )
    }
}

fun List<LocalArticle>.mapToArticle(): List<Article> {
    return this.map {
        Article(
            uuid = it.uuid,
            num = it.num,
            title = it.title,
            writer = it.writer,
            content = it.content,
            date = it.date,
            articleUrl = it.articleUrl,
            department = it.department,
            board = it.board,
            read = it.read,
            isNotice = it.isNotice,
            receivedTime = it.receivedTime
        )
    }
}

fun LocalArticle.mapToArticle(): Article {
    return Article(
        uuid = this.uuid,
        num = this.num,
        title = this.title,
        writer = this.writer,
        content = this.content,
        date = this.date,
        articleUrl = this.articleUrl,
        department = this.department,
        board = this.board,
        read = this.read,
        isNotice = this.isNotice,
        receivedTime = this.receivedTime
    )
}

fun Article.mapToLocalArticle(): LocalArticle {
    return LocalArticle(
        uuid = this.uuid,
        num = this.num,
        title = this.title,
        writer = this.writer,
        content = this.content,
        date = this.date,
        articleUrl = this.articleUrl,
        department = this.department,
        board = this.board,
        read = this.read,
        isNotice = this.isNotice,
        receivedTime = this.receivedTime
    )
}

fun ArticleResponse.mapToArticle(department: String, board: String): Article {
    return Article(
        uuid = this.uuid,
        num = this.num,
        title = this.title,
        writer = this.writer,
        content = this.content,
        date = this.date,
        articleUrl = this.articleUrl,
        department = department,
        board = board,
        read = false,
        isNotice = this.isNotice,
        receivedTime = TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis())
    )
}
