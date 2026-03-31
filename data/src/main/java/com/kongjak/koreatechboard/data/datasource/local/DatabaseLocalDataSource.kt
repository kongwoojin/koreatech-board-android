package com.kongjak.koreatechboard.data.datasource.local

import com.kongjak.koreatechboard.data.dao.ArticleDao
import com.kongjak.koreatechboard.data.enity.Article
import com.kongjak.koreatechboard.data.mapper.mapToLocalArticle
import com.kongjak.koreatechboard.domain.model.LocalArticle
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class DatabaseLocalDataSource @Inject constructor(
    private val articleDao: ArticleDao
) {
    fun getArticleList(vararg departments: String): Flow<List<Article>> {
        return articleDao.getAll(*departments)
    }

    fun getArticle(uuid: UUID): LocalArticle {
        return articleDao.getArticle(uuid).mapToLocalArticle()
    }

    fun insertArticle(article: Article) {
        articleDao.insert(article)
    }

    fun deleteArticle(uuid: UUID) {
        articleDao.delete(uuid)
    }

    fun deleteAllArticle() {
        articleDao.deleteAll()
    }

    fun updateRead(uuid: UUID, read: Boolean) {
        articleDao.updateRead(uuid, read)
    }
}
