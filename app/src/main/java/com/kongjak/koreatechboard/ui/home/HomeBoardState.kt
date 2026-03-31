package com.kongjak.koreatechboard.ui.home

import com.kongjak.koreatechboard.domain.model.BoardData

data class HomeBoardState(
    val boardData: Map<String, HomeBoardData> = emptyMap()
) {
    data class HomeBoardData(
        val isLoaded: Boolean = false,
        val isSuccess: Boolean = false,
        val boardData: List<BoardData> = emptyList(),
        val statusCode: Int = 0,
        val error: String = ""
    )
}
