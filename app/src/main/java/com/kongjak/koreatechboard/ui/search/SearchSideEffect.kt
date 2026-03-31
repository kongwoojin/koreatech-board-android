package com.kongjak.koreatechboard.ui.search

sealed class SearchSideEffect {
    data class FetchData(val department: String, val board: String, val title: String) :
        SearchSideEffect()
}
