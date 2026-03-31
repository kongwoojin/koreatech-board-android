package com.kongjak.koreatechboard.ui.board

sealed class BoardSideEffect {
    data class FetchData(val department: String, val board: String) : BoardSideEffect()
}
