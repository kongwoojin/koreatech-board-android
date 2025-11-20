package com.kongjak.koreatechboard.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kongjak.koreatechboard.R
import com.kongjak.koreatechboard.domain.usecase.api.GetBoardMinimumUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
class HomeBoardViewModel @Inject constructor(private val getBoardMinimumUseCase: GetBoardMinimumUseCase) :
    ContainerHost<HomeBoardState, HomeBoardSideEffect>, ViewModel() {

    override val container = container<HomeBoardState, HomeBoardSideEffect>(HomeBoardState())

    private val isFetching = AtomicBoolean(false)

    suspend fun getApi(department: String, board: String) = viewModelScope.launch {
        while (isFetching.get()) {
            delay(1000)
        }
        intent {
            if (board in state.boardData && state.boardData[board]?.boardData!!.isNotEmpty()) return@intent
            postSideEffect(HomeBoardSideEffect.FetchData(department, board))
        }
    }

    fun handleSideEffect(sideEffect: HomeBoardSideEffect, getString: (Int) -> String) {
        when (sideEffect) {
            is HomeBoardSideEffect.FetchData -> {
                viewModelScope.launch {
                    isFetching.set(true)
                    intent {
                        reduce {
                            state.copy(
                                boardData = state.boardData + (
                                    sideEffect.board to (
                                        state.boardData[sideEffect.board]?.copy(
                                            isLoaded = false
                                        ) ?: HomeBoardState.HomeBoardData(
                                            isLoaded = false
                                        )
                                        )
                                    )
                            )
                        }
                    }

                    runCatching {
                        getBoardMinimumUseCase(sideEffect.department, sideEffect.board).getOrNull()
                    }.onSuccess {
                        intent {
                            reduce {
                                state.copy(
                                    boardData = state.boardData + (
                                        sideEffect.board to (
                                            state.boardData[sideEffect.board]?.copy(
                                                isSuccess = true,
                                                boardData = it?.boardData ?: emptyList(),
                                                statusCode = it?.statusCode ?: 200,
                                                isLoaded = true
                                            ) ?: HomeBoardState.HomeBoardData(
                                                isSuccess = true,
                                                boardData = it?.boardData
                                                    ?: emptyList(),
                                                statusCode = it?.statusCode ?: 200,
                                                isLoaded = true
                                            )
                                            )
                                        )
                                )
                            }
                        }
                    }.onFailure {
                        val errorMessage = when (it) {
                            is SocketTimeoutException,
                            is UnknownHostException -> {
                                getString(R.string.error_timeout)
                            }

                            else -> it.message ?: getString(R.string.error_unknown)
                        }
                        intent {
                            reduce {
                                state.copy(
                                    boardData = state.boardData + (
                                        sideEffect.board to (
                                            state.boardData[sideEffect.board]?.copy(
                                                isSuccess = false,
                                                error = errorMessage,
                                                isLoaded = true
                                            ) ?: HomeBoardState.HomeBoardData(
                                                isSuccess = false,
                                                error = errorMessage,
                                                isLoaded = true
                                            )
                                            )
                                        )
                                )
                            }
                        }
                    }
                    isFetching.set(false)
                }
            }
        }
    }
}
