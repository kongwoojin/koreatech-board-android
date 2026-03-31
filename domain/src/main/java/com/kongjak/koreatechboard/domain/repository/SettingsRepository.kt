package com.kongjak.koreatechboard.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun setUserDepartment(index: Int)
    fun getUserDepartment(): Flow<Int>
    suspend fun setInitDepartment(index: Int)
    fun getInitDepartment(): Flow<Int>
    suspend fun setDynamicTheme(state: Boolean)
    fun getDynamicTheme(): Flow<Boolean>
    suspend fun setDarkTheme(theme: Int)
    fun getDarkTheme(): Flow<Int>
    suspend fun setSchoolNoticeSubscribe(state: Boolean)
    fun getSchoolNoticeSubscribe(): Flow<Boolean>
    suspend fun setDormNoticeSubscribe(state: Boolean)
    fun getDormNoticeSubscribe(): Flow<Boolean>
    suspend fun setDepartmentNoticeSubscribe(state: Boolean)
    fun getDepartmentNoticeSubscribe(): Flow<Boolean>
}
