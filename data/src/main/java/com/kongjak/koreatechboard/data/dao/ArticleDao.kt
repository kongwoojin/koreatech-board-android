package com.kongjak.koreatechboard.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.kongjak.koreatechboard.data.enity.Article
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ArticleDao {
    @Transaction
    @Query("SELECT * FROM article WHERE department IN (:departments) ORDER BY received_time DESC, write_date DESC, num DESC")
    fun getAll(vararg departments: String): Flow<List<Article>>

    @Query("SELECT * FROM article WHERE uuid = :uuid")
    fun getArticle(uuid: UUID): Article

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(article: Article)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg articles: Article)

    @Query("UPDATE article SET read = :read WHERE uuid = :uuid")
    fun updateRead(uuid: UUID, read: Boolean)

    @Query("DELETE FROM article WHERE uuid = :uuid")
    fun delete(uuid: UUID)

    @Query("DELETE FROM article")
    fun deleteAll()
}
