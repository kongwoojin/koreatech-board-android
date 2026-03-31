package com.kongjak.koreatechboard.ui.main

data class MainState(
    val initDepartment: Int = 0,
    val userDepartment: Int = 0,
    val isDynamicTheme: Boolean = true,
    val isDarkTheme: Boolean? = null,
    val externalLink: String? = null
)
