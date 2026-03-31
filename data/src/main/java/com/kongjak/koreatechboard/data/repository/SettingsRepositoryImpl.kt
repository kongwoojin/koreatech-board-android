package com.kongjak.koreatechboard.data.repository

import com.kongjak.koreatechboard.data.datasource.local.SettingsLocalDataSource
import com.kongjak.koreatechboard.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingsLocalDataSource: SettingsLocalDataSource
) : SettingsRepository {
    override suspend fun setUserDepartment(index: Int) {
        settingsLocalDataSource.setUserDepartment(index)
    }
    override fun getUserDepartment(): Flow<Int> = settingsLocalDataSource.getUserDepartment()

    override suspend fun setInitDepartment(index: Int) {
        settingsLocalDataSource.setInitDepartment(index)
    }

    override fun getInitDepartment(): Flow<Int> = settingsLocalDataSource.getInitDepartment()

    override suspend fun setDynamicTheme(state: Boolean) {
        settingsLocalDataSource.setDynamicTheme(state)
    }
    override fun getDynamicTheme(): Flow<Boolean> {
        return settingsLocalDataSource.getDynamicTheme()
    }

    override suspend fun setDarkTheme(theme: Int) {
        settingsLocalDataSource.setDarkTheme(theme)
    }

    override fun getDarkTheme(): Flow<Int> {
        return settingsLocalDataSource.getDarkTheme()
    }

    override suspend fun setSchoolNoticeSubscribe(state: Boolean) {
        settingsLocalDataSource.setSchoolNoticeSubscribe(state)
    }

    override fun getSchoolNoticeSubscribe(): Flow<Boolean> {
        return settingsLocalDataSource.getSchoolNoticeSubscribe()
    }

    override suspend fun setDormNoticeSubscribe(state: Boolean) {
        settingsLocalDataSource.setDormNoticeSubscribe(state)
    }

    override fun getDormNoticeSubscribe(): Flow<Boolean> {
        return settingsLocalDataSource.getDormNoticeSubscribe()
    }

    override suspend fun setDepartmentNoticeSubscribe(state: Boolean) {
        settingsLocalDataSource.setDepartmentNoticeSubscribe(state)
    }

    override fun getDepartmentNoticeSubscribe(): Flow<Boolean> {
        return settingsLocalDataSource.getDepartmentNoticeSubscribe()
    }
}
