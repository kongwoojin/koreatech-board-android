package com.kongjak.koreatechboard.ui.board

import androidx.paging.PagingData
import com.kongjak.koreatechboard.domain.model.BoardData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class BoardState(
    val department: String = "",
    val board: String = "",
    val boardItem: Flow<PagingData<BoardData>> = emptyFlow(),
    val isInitialized: Boolean = false
)
