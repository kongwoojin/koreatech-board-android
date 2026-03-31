package com.kongjak.koreatechboard.data.datasource.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kongjak.koreatechboard.domain.DARK_THEME_SYSTEM_DEFAULT
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsLocalDataSource @Inject constructor(
    private val context: Context
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private val userDepartmentKey = intPreferencesKey("user_department")
    private val initDepartmentKey = intPreferencesKey("init_department")
    private val dynamicThemeKey = booleanPreferencesKey("dynamic_theme")
    private val darkThemeKey = intPreferencesKey("dark_theme")
    private val schoolNoticeSubscribe = booleanPreferencesKey("school_notice_subscribe")
    private val dormNoticeSubscribe = booleanPreferencesKey("dorm_notice_subscribe")
    private val departmentNoticeSubscribe = booleanPreferencesKey("department_notice_subscribe")

    suspend fun setUserDepartment(newIndex: Int) {
        context.dataStore.edit { preferences ->
            preferences[userDepartmentKey] = newIndex
        }
    }

    fun getUserDepartment(): Flow<Int> {
        val departmentFlow: Flow<Int> = context.dataStore.data
            .map { preferences ->
                preferences[userDepartmentKey] ?: 0
            }

        return departmentFlow
    }

    suspend fun setInitDepartment(newIndex: Int) {
        context.dataStore.edit { preferences ->
            preferences[initDepartmentKey] = newIndex
        }
    }

    fun getInitDepartment(): Flow<Int> {
        val departmentFlow: Flow<Int> = context.dataStore.data
            .map { preferences ->
                preferences[initDepartmentKey] ?: 0
            }

        return departmentFlow
    }

    suspend fun setDynamicTheme(newState: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[dynamicThemeKey] = newState
        }
    }

    fun getDynamicTheme(): Flow<Boolean> =
        context.dataStore.data
            .map { preferences ->
                preferences[dynamicThemeKey] ?: true
            }

    suspend fun setDarkTheme(newTheme: Int) {
        context.dataStore.edit { preferences ->
            preferences[darkThemeKey] = newTheme
        }
    }

    fun getDarkTheme(): Flow<Int> =
        context.dataStore.data
            .map { preferences ->
                preferences[darkThemeKey] ?: DARK_THEME_SYSTEM_DEFAULT
            }

    suspend fun setSchoolNoticeSubscribe(state: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[schoolNoticeSubscribe] = state
        }
    }

    fun getSchoolNoticeSubscribe(): Flow<Boolean> =
        context.dataStore.data
            .map { preferences ->
                preferences[schoolNoticeSubscribe] ?: false
            }

    suspend fun setDormNoticeSubscribe(state: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[dormNoticeSubscribe] = state
        }
    }

    fun getDormNoticeSubscribe(): Flow<Boolean> =
        context.dataStore.data
            .map { preferences ->
                preferences[dormNoticeSubscribe] ?: false
            }

    suspend fun setDepartmentNoticeSubscribe(state: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[departmentNoticeSubscribe] = state
        }
    }

    fun getDepartmentNoticeSubscribe(): Flow<Boolean> =
        context.dataStore.data
            .map { preferences ->
                preferences[departmentNoticeSubscribe] ?: false
            }
}
