package com.kongjak.koreatechboard.ui.article

import androidx.lifecycle.viewModelScope
import com.benasher44.uuid.Uuid
import com.kongjak.koreatechboard.domain.usecase.api.GetArticleUseCase
import com.kongjak.koreatechboard.util.ViewModelExt
import io.ktor.client.plugins.HttpRequestTimeoutException
import koreatech_board.app.generated.resources.Res
import koreatech_board.app.generated.resources.error_timeout
import koreatech_board.app.generated.resources.error_unknown
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import java.net.UnknownHostException

class ArticleViewModel(
    private val getArticleUseCase: GetArticleUseCase
) : ViewModelExt<ArticleState, ArticleSideEffect>(ArticleState()) {

    fun getArticleData(department: String, uuid: Uuid) {
        intent {
            postSideEffect(ArticleSideEffect.FetchData(department, uuid))
        }
    }

    fun handleSideEffect(sideEffect: ArticleSideEffect) {
        when (sideEffect) {
            is ArticleSideEffect.FetchData -> {
                viewModelScope.launch {
                    intent {
                        reduce {
                            state.copy(
                                isLoading = true,
                                isLoaded = false
                            )
                        }
                    }
                    runCatching {
                        getArticleUseCase(sideEffect.uuid).getOrNull()
                    }.onSuccess {
                        intent {
                            reduce {
                                state.copy(
                                    isSuccess = true,
                                    isLoading = false,
                                    article = it,
                                    isLoaded = true
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
                                    isSuccess = false,
                                    isLoading = false,
                                    error = errorMessage,
                                    isLoaded = true
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
