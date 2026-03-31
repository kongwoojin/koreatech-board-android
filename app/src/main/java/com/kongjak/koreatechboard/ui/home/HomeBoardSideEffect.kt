package com.kongjak.koreatechboard.ui.home

sealed class HomeBoardSideEffect {
    data class FetchData(val department: String, val board: String) : HomeBoardSideEffect()
}
