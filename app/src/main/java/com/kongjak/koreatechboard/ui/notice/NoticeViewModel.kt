package com.kongjak.koreatechboard.ui.notice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kongjak.koreatechboard.domain.usecase.database.DeleteNewNoticeUseCase
import com.kongjak.koreatechboard.domain.usecase.database.GetAllNewNoticesUseCase
import com.kongjak.koreatechboard.domain.usecase.database.UpdateNewNoticeReadUseCase
import com.kongjak.koreatechboard.domain.usecase.settings.department.GetUserDepartmentUseCase
import com.kongjak.koreatechboard.ui.settings.deptList
import com.kongjak.koreatechboard.util.routes.Department
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class NoticeViewModel @Inject constructor(
    private val getAllNewNoticesUseCase: GetAllNewNoticesUseCase,
    private val updateNewNoticeReadUseCase: UpdateNewNoticeReadUseCase,
    private val deleteNewNoticeUseCase: DeleteNewNoticeUseCase,
    private val getUserDepartmentUseCase: GetUserDepartmentUseCase
) : ContainerHost<NoticeState, NoticeSideEffect>, ViewModel() {
    override val container = container<NoticeState, NoticeSideEffect>(NoticeState())

    init {
        viewModelScope.launch {
            getUserDepartmentUseCase().collectLatest {
                intent {
                    reduce {
                        state.copy(userDepartment = it)
                    }
                }
            }
        }
    }

    fun getAllNotices() {
        intent {
            val noticesDeptList = listOf(
                Department.School,
                Department.Dorm,
                deptList[state.userDepartment]
            )
            postSideEffect(NoticeSideEffect.GetAllNotices(state.selectedDepartment.map { noticesDeptList[it].name }))
        }
    }

    fun updateRead(uuid: UUID, read: Boolean) {
        intent {
            postSideEffect(NoticeSideEffect.UpdateRead(uuid, read))
        }
    }

    fun deleteNotice(uuid: UUID) {
        intent {
            postSideEffect(NoticeSideEffect.DeleteNotice(uuid))
        }
    }

    fun handleSideEffect(sideEffect: NoticeSideEffect) {
        when (sideEffect) {
            is NoticeSideEffect.GetAllNotices -> viewModelScope.launch(Dispatchers.IO) {
                intent {
                    getAllNewNoticesUseCase(*(sideEffect.departments.toTypedArray())).collect {
                        reduce {
                            state.copy(articles = it, isLoaded = true)
                        }
                    }
                }
            }

            is NoticeSideEffect.UpdateRead -> viewModelScope.launch(Dispatchers.IO) {
                updateNewNoticeReadUseCase(sideEffect.uuid, sideEffect.read)
            }

            is NoticeSideEffect.DeleteNotice -> viewModelScope.launch(Dispatchers.IO) {
                deleteNewNoticeUseCase(sideEffect.uuid)
            }

            is NoticeSideEffect.AddSelectedDepartment -> intent {
                reduce {
                    state.copy(selectedDepartment = state.selectedDepartment + sideEffect.department)
                }
            }

            is NoticeSideEffect.RemoveSelectedDepartment -> {
                intent {
                    reduce {
                        state.copy(selectedDepartment = state.selectedDepartment - sideEffect.department)
                    }
                }
            }
        }
    }

    fun addSelectedDepartment(department: Int) = intent {
        postSideEffect(NoticeSideEffect.AddSelectedDepartment(department))
    }

    fun removeSelectedDepartment(department: Int) = intent {
        postSideEffect(NoticeSideEffect.RemoveSelectedDepartment(department))
    }
}
