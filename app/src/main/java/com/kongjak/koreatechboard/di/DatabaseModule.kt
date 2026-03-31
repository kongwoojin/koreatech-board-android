package com.kongjak.koreatechboard.di

import android.content.Context
import androidx.room.Room
import com.kongjak.koreatechboard.data.dao.ArticleDao
import com.kongjak.koreatechboard.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun provideArticleDao(appDatabase: AppDatabase): ArticleDao = appDatabase.articleDao()

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room
        .databaseBuilder(context, AppDatabase::class.java, "koreatech_board_article.db")
        .build()
}
