package com.kongjak.koreatechboard.di

import com.kongjak.koreatechboard.data.api.API
import com.kongjak.koreatechboard.data.datasource.local.DatabaseLocalDataSource
import com.kongjak.koreatechboard.data.datasource.local.SettingsLocalDataSource
import com.kongjak.koreatechboard.data.datasource.remote.ArticleRemoteDataSource
import com.kongjak.koreatechboard.data.datasource.remote.BoardRemoteDataSource
import com.kongjak.koreatechboard.data.repository.ArticleRepositoryImpl
import com.kongjak.koreatechboard.data.repository.BoardRepositoryImpl
import com.kongjak.koreatechboard.data.repository.DatabaseRepositoryImpl
import com.kongjak.koreatechboard.data.repository.SettingsRepositoryImpl
import com.kongjak.koreatechboard.domain.repository.ArticleRepository
import com.kongjak.koreatechboard.domain.repository.BoardRepository
import com.kongjak.koreatechboard.domain.repository.DatabaseRepository
import com.kongjak.koreatechboard.domain.repository.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideArticleRepository(
        articleRemoteDataSource: ArticleRemoteDataSource
    ): ArticleRepository {
        return ArticleRepositoryImpl(articleRemoteDataSource)
    }

    @Singleton
    @Provides
    fun provideBoardRepository(
        boardRemoteDataSource: BoardRemoteDataSource,
        api: API
    ): BoardRepository {
        return BoardRepositoryImpl(boardRemoteDataSource, api)
    }

    @Singleton
    @Provides
    fun provideSettingsRepository(
        settingsLocalDataSource: SettingsLocalDataSource
    ): SettingsRepository {
        return SettingsRepositoryImpl(settingsLocalDataSource)
    }

    @Singleton
    @Provides
    fun provideDatabaseRepository(
        databaseLocalDataSource: DatabaseLocalDataSource,
        articleRemoteDataSource: ArticleRemoteDataSource
    ): DatabaseRepository {
        return DatabaseRepositoryImpl(databaseLocalDataSource, articleRemoteDataSource)
    }
}
