package com.kongjak.koreatechboard.ui.main

sealed class MainSideEffect {
    data object InitDepartmentUpdate : MainSideEffect()
    data object UserDepartmentUpdate : MainSideEffect()
    data object GetDynamicTheme : MainSideEffect()
    data object GetDarkTheme : MainSideEffect()
    data class SetExternalLink(val url: String) : MainSideEffect()
    data class SetSubscribe(val subscribe: Boolean) : MainSideEffect()
}
