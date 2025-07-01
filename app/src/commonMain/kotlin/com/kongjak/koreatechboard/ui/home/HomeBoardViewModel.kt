package com.kongjak.koreatechboard.ui.home

import androidx.lifecycle.viewModelScope
import com.kongjak.koreatechboard.domain.usecase.api.GetBoardMinimumUseCase
import com.kongjak.koreatechboard.util.ViewModelExt
import io.ktor.client.plugins.HttpRequestTimeoutException
import koreatech_board.app.generated.resources.Res
import koreatech_board.app.generated.resources.error_timeout
import koreatech_board.app.generated.resources.error_unknown
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import java.net.UnknownHostException
import java.util.concurrent.atomic.AtomicBoolean

class HomeBoardViewModel(private val getBoardMinimumUseCase: GetBoardMinimumUseCase) :
    ViewModelExt<HomeBoardState, HomeBoardEvent>(HomeBoardState()) {

    private val isFetching = AtomicBoolean(false)

    suspend fun getApi(department: String, board: String) = viewModelScope.launch {
        while (isFetching.get()) {
            delay(1000)
        }
        intent {
            if (board in state.boardData && state.boardData[board]?.boardData!!.isNotEmpty()) return@intent
            postSideEffect(HomeBoardEvent.FetchData(department, board))
        }
    }

    fun handleSideEffect(sideEffect: HomeBoardEvent) {
        when (sideEffect) {
            is HomeBoardEvent.FetchData -> {
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
                        val errorMessage = when (it.cause) {
                            is java.net.SocketTimeoutException,
                            is UnknownHostException,
                            is HttpRequestTimeoutException -> {
                                getString(Res.string.error_timeout)
                            }

                            else -> it.message ?: getString(Res.string.error_unknown)
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
