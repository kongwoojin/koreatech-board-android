package com.kongjak.koreatechboard.ui.board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.kongjak.koreatechboard.domain.usecase.api.GetBoardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class BoardViewModel @Inject constructor(
    private val getBoardUseCase: GetBoardUseCase
) : ContainerHost<BoardState, BoardSideEffect>, ViewModel() {

    override val container = container<BoardState, BoardSideEffect>(BoardState())

    fun getAPI(department: String, board: String) {
        intent {
            if (state.department == department && state.board == board) return@intent
            postSideEffect(BoardSideEffect.FetchData(department, board))
        }
    }

    fun handleSideEffect(sideEffect: BoardSideEffect) {
        when (sideEffect) {
            is BoardSideEffect.FetchData -> {
                intent {
                    reduce {
                        state.copy(
                            department = sideEffect.department,
                            board = sideEffect.board,
                            boardItem = getBoardUseCase(
                                sideEffect.department,
                                sideEffect.board
                            ).cachedIn(
                                viewModelScope
                            ),
                            isInitialized = true
                        )
                    }
                }
            }
        }
    }
}
