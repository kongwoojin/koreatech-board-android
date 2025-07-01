package com.kongjak.koreatechboard.data.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.kongjak.koreatechboard.data.AppDatabase
import com.kongjak.koreatechboard.data.enity.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ArticleDaoImpl(
    db: AppDatabase
) : ArticleDao {
    private val queries = db.noticeQueries

    override fun getAll(vararg departments: String): Flow<List<Article>> {
        return queries.getAll { uuid, num, title, writer, content, writeDate, articleUrl, department, board, read, isNotice, receivedTime ->
            Article(
                uuid = Uuid.parse(uuid),
                num = num.toInt(),
                title = title,
                writer = writer,
                content = content,
                date = writeDate,
                articleUrl = articleUrl,
                department = department,
                board = board,
                read = read.toInt() == 1,
                isNotice = isNotice.toInt() == 1,
                receivedTime = receivedTime
            )
        }.asFlow().mapToList(Dispatchers.IO)
    }

    override fun getArticle(uuid: Uuid): Article {
        return queries.getArticle(uuid.toString()).executeAsOne().let {
            Article(
                uuid = Uuid.parse(it.uuid),
                num = it.num.toInt(),
                title = it.title,
                writer = it.writer,
                content = it.content,
                date = it.write_date,
                articleUrl = it.article_url,
                department = it.department,
                board = it.board,
                read = it.read.toInt() == 1,
                isNotice = it.is_notice.toInt() == 1,
                receivedTime = it.received_time
            )
        }
    }

    override fun insert(article: Article) {
        queries.insert(
            uuid = article.uuid.toString(),
            num = article.num.toLong(),
            title = article.title,
            writer = article.writer,
            content = article.content,
            write_date = article.date,
            article_url = article.articleUrl,
            department = article.department,
            board = article.board,
            read = if (article.read) 1 else 0,
            is_notice = if (article.isNotice) 1 else 0,
            received_time = article.receivedTime
        )
    }

    override fun insertAll(vararg articles: Article) {
        articles.forEach {
            insert(it)
        }
    }

    override fun updateRead(uuid: Uuid, read: Boolean) {
        queries.updateRead(if (read) 1 else 0, uuid.toString())
    }

    override fun delete(uuid: Uuid) {
        queries.delete(uuid.toString())
    }

    override fun deleteAll() {
        queries.deleteAll()
    }
}
