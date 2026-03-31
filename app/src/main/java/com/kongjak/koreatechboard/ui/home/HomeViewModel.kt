package com.kongjak.koreatechboard.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kongjak.koreatechboard.domain.usecase.settings.department.GetUserDepartmentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserDepartmentUseCase: GetUserDepartmentUseCase
) : ContainerHost<HomeState, HomeSideEffect>, ViewModel() {

    override val container = container<HomeState, HomeSideEffect>(HomeState())

    init {
        getDepartment()
    }

    private fun getDepartment() {
        intent { postSideEffect(HomeSideEffect.GetDepartment) }
    }

    fun handleSideEffect(sideEffect: HomeSideEffect) {
        when (sideEffect) {
            HomeSideEffect.GetDepartment -> viewModelScope.launch {
                getUserDepartmentUseCase().collectLatest {
                    intent {
                        reduce {
                            state.copy(department = it)
                        }
                    }
                }
            }
        }
    }
}
