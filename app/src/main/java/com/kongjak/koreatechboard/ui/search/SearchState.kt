package com.kongjak.koreatechboard.ui.search

import androidx.paging.PagingData
import com.kongjak.koreatechboard.domain.model.BoardData
import com.kongjak.koreatechboard.util.routes.BoardItem
import com.kongjak.koreatechboard.util.routes.Department
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class SearchState(
    val department: String = Department.School.name,
    val board: String = BoardItem.Notice.board,
    val title: String = "",
    val boardData: Flow<PagingData<BoardData>> = emptyFlow()
)
