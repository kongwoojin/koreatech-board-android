package com.kongjak.koreatechboard.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kongjak.koreatechboard.data.converter.UUIDConverter
import com.kongjak.koreatechboard.data.dao.ArticleDao
import com.kongjak.koreatechboard.data.enity.Article

@Database(entities = [Article::class], version = 1)
@TypeConverters(UUIDConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}
