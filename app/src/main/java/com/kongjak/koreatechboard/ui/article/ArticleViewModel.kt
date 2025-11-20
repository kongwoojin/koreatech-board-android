package com.kongjak.koreatechboard.ui.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kongjak.koreatechboard.R
import com.kongjak.koreatechboard.domain.usecase.api.GetArticleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.net.UnknownHostException
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val getArticleUseCase: GetArticleUseCase
) : ContainerHost<ArticleState, ArticleSideEffect>, ViewModel() {

    override val container = container<ArticleState, ArticleSideEffect>(ArticleState())

    fun getArticleData(department: String, uuid: UUID) {
        intent {
            postSideEffect(ArticleSideEffect.FetchData(department, uuid))
        }
    }

    fun handleSideEffect(sideEffect: ArticleSideEffect, getString: (Int) -> String) {
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
                        val errorMessage = when (it) {
                            is java.net.SocketTimeoutException,
                            is UnknownHostException -> {
                                getString(R.string.error_timeout)
                            }

                            else -> it.message ?: getString(R.string.error_unknown)
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
