package com.kongjak.koreatechboard.di

import android.content.Context
import com.kongjak.koreatechboard.data.dao.ArticleDao
import com.kongjak.koreatechboard.data.datasource.local.DatabaseLocalDataSource
import com.kongjak.koreatechboard.data.datasource.local.SettingsLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Singleton
    @Provides
    fun provideSettingsLocalDataSource(
        @ApplicationContext context: Context
    ): SettingsLocalDataSource {
        return SettingsLocalDataSource(context)
    }

    @Singleton
    @Provides
    fun provideDatabaseLocalDataSource(
        articleDao: ArticleDao
    ): DatabaseLocalDataSource {
        return DatabaseLocalDataSource(articleDao)
    }
}
