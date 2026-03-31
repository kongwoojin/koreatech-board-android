package com.kongjak.koreatechboard.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.kongjak.koreatechboard.domain.usecase.api.SearchBoardWithTitleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchBoardWithTitleUseCase: SearchBoardWithTitleUseCase) :
    ContainerHost<SearchState, SearchSideEffect>, ViewModel() {

    override val container = container<SearchState, SearchSideEffect>(SearchState())

    fun getAPI(department: String, board: String, title: String) {
        intent {
            postSideEffect(SearchSideEffect.FetchData(department, board, title))
        }
    }

    fun handleSideEffect(sideEffect: SearchSideEffect) {
        when (sideEffect) {
            is SearchSideEffect.FetchData -> {
                intent {
                    reduce {
                        state.copy(
                            department = sideEffect.department,
                            board = sideEffect.board,
                            title = sideEffect.title,
                            boardData = searchBoardWithTitleUseCase(
                                sideEffect.department,
                                sideEffect.board,
                                sideEffect.title
                            ).cachedIn(
                                viewModelScope
                            )
                        )
                    }
                }
            }
        }
    }
}
