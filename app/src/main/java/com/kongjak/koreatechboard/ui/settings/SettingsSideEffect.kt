package com.kongjak.koreatechboard.ui.settings

sealed class SettingsSideEffect {
    object GetUserDepartment : SettingsSideEffect()
    data class SetUserDepartment(val index: Int) : SettingsSideEffect()
    object GetInitDepartment : SettingsSideEffect()
    data class SetInitDepartment(val index: Int) : SettingsSideEffect()
    object GetDynamicTheme : SettingsSideEffect()
    data class SetDynamicTheme(val state: Boolean) : SettingsSideEffect()
    object GetDarkTheme : SettingsSideEffect()
    data class SetDarkTheme(val index: Int) : SettingsSideEffect()
    data class UpdateSchoolSubscribe(val subscribe: Boolean) : SettingsSideEffect()
    data class UpdateDormSubscribe(val subscribe: Boolean) : SettingsSideEffect()
    data class UpdateDepartmentSubscribe(val subscribe: Boolean) : SettingsSideEffect()
    object DeleteAllNewArticle : SettingsSideEffect()
    object GetSchoolSubscribe : SettingsSideEffect()
    object GetDormSubscribe : SettingsSideEffect()
    object GetDepartmentSubscribe : SettingsSideEffect()
}
