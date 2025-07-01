package com.kongjak.koreatechboard.ui.settings

import androidx.lifecycle.viewModelScope
import com.kongjak.koreatechboard.constant.FCM_TOPIC_DORM
import com.kongjak.koreatechboard.constant.FCM_TOPIC_SCHOOL
import com.kongjak.koreatechboard.domain.usecase.database.DeleteAllNewNoticesUseCase
import com.kongjak.koreatechboard.domain.usecase.settings.department.GetInitDepartmentUseCase
import com.kongjak.koreatechboard.domain.usecase.settings.department.GetUserDepartmentUseCase
import com.kongjak.koreatechboard.domain.usecase.settings.department.SetInitDepartmentUseCase
import com.kongjak.koreatechboard.domain.usecase.settings.department.SetUserDepartmentUseCase
import com.kongjak.koreatechboard.domain.usecase.settings.subscribe.GetDepartmentNoticeSubscribe
import com.kongjak.koreatechboard.domain.usecase.settings.subscribe.GetDormNoticeSubscribe
import com.kongjak.koreatechboard.domain.usecase.settings.subscribe.GetSchoolNoticeSubscribe
import com.kongjak.koreatechboard.domain.usecase.settings.subscribe.SetDepartmentNoticeSubscribe
import com.kongjak.koreatechboard.domain.usecase.settings.subscribe.SetDormNoticeSubscribe
import com.kongjak.koreatechboard.domain.usecase.settings.subscribe.SetSchoolNoticeSubscribe
import com.kongjak.koreatechboard.domain.usecase.settings.theme.GetDarkThemeUseCase
import com.kongjak.koreatechboard.domain.usecase.settings.theme.GetDynamicThemeUseCase
import com.kongjak.koreatechboard.domain.usecase.settings.theme.SetDarkThemeUseCase
import com.kongjak.koreatechboard.domain.usecase.settings.theme.SetDynamicThemeUseCase
import com.kongjak.koreatechboard.util.ViewModelExt
import com.kongjak.koreatechboard.util.getPlatformInfo
import com.kongjak.koreatechboard.util.subscribeFirebaseTopic
import com.kongjak.koreatechboard.util.unsubscribeFirebaseTopic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce

class SettingsViewModel(
    private val getUserDepartmentUseCase: GetUserDepartmentUseCase,
    private val setUserDepartmentUseCase: SetUserDepartmentUseCase,
    private val getInitDepartmentUseCase: GetInitDepartmentUseCase,
    private val setInitDepartmentUseCase: SetInitDepartmentUseCase,
    private val getDynamicThemeUseCase: GetDynamicThemeUseCase,
    private val setDynamicThemeUseCase: SetDynamicThemeUseCase,
    private val getDarkThemeUseCase: GetDarkThemeUseCase,
    private val setDarkThemeUseCase: SetDarkThemeUseCase,
    private val setSchoolNoticeSubscribe: SetSchoolNoticeSubscribe,
    private val getSchoolNoticeSubscribe: GetSchoolNoticeSubscribe,
    private val setDormNoticeSubscribe: SetDormNoticeSubscribe,
    private val getDormNoticeSubscribe: GetDormNoticeSubscribe,
    private val setDepartmentNoticeSubscribe: SetDepartmentNoticeSubscribe,
    private val getDepartmentNoticeSubscribe: GetDepartmentNoticeSubscribe,
    private val deleteAllNewNoticesUseCase: DeleteAllNewNoticesUseCase
) : ViewModelExt<SettingsState, SettingsSideEffect>(SettingsState()) {

    init {
        intent {
            postSideEffect(SettingsSideEffect.GetUserDepartment)
            postSideEffect(SettingsSideEffect.GetInitDepartment)
            if (getPlatformInfo().isDynamicThemeSupported) {
                postSideEffect(SettingsSideEffect.GetDynamicTheme)
            }
            postSideEffect(SettingsSideEffect.GetDarkTheme)
            postSideEffect(SettingsSideEffect.GetSchoolSubscribe)
            postSideEffect(SettingsSideEffect.GetDormSubscribe)
            postSideEffect(SettingsSideEffect.GetDepartmentSubscribe)
        }
    }

    fun sendSideEffect(sideEffect: SettingsSideEffect) {
        intent {
            postSideEffect(sideEffect)
        }
    }

    fun handleSideEffect(sideEffect: SettingsSideEffect) {
        when (sideEffect) {
            SettingsSideEffect.GetDarkTheme -> viewModelScope.launch {
                getDarkThemeUseCase().collectLatest {
                    intent {
                        reduce {
                            state.copy(isDarkTheme = it)
                        }
                    }
                }
            }

            SettingsSideEffect.GetDynamicTheme -> viewModelScope.launch {
                getDynamicThemeUseCase().collectLatest {
                    intent {
                        reduce {
                            state.copy(isDynamicTheme = it)
                        }
                    }
                }
            }

            SettingsSideEffect.GetInitDepartment -> viewModelScope.launch {
                getInitDepartmentUseCase().collectLatest {
                    intent {
                        reduce {
                            state.copy(initDepartment = it)
                        }
                    }
                }
            }

            SettingsSideEffect.GetUserDepartment -> viewModelScope.launch {
                getUserDepartmentUseCase().collectLatest {
                    intent {
                        reduce {
                            state.copy(userDepartment = it)
                        }
                    }
                }
            }

            is SettingsSideEffect.SetDarkTheme -> viewModelScope.launch {
                setDarkThemeUseCase(sideEffect.index)
                intent {
                    reduce {
                        state.copy(isDarkTheme = sideEffect.index)
                    }
                }
            }

            is SettingsSideEffect.SetDynamicTheme -> viewModelScope.launch {
                setDynamicThemeUseCase(sideEffect.state)
                intent {
                    reduce {
                        state.copy(isDynamicTheme = sideEffect.state)
                    }
                }
            }

            is SettingsSideEffect.SetInitDepartment -> viewModelScope.launch {
                setInitDepartmentUseCase(sideEffect.index)
                intent {
                    reduce {
                        state.copy(initDepartment = sideEffect.index)
                    }
                }
            }

            is SettingsSideEffect.SetUserDepartment -> viewModelScope.launch {
                setUserDepartmentUseCase(sideEffect.index)
                intent {
                    reduce {
                        state.copy(userDepartment = sideEffect.index)
                    }
                }
            }

            is SettingsSideEffect.UpdateSchoolSubscribe -> {
                viewModelScope.launch {
                    setSchoolNoticeSubscribe(sideEffect.subscribe)
                }
            }

            is SettingsSideEffect.UpdateDormSubscribe -> {
                viewModelScope.launch {
                    setDormNoticeSubscribe(sideEffect.subscribe)
                }
            }

            is SettingsSideEffect.UpdateDepartmentSubscribe -> {
                viewModelScope.launch {
                    setDepartmentNoticeSubscribe(sideEffect.subscribe)
                }
            }

            SettingsSideEffect.GetSchoolSubscribe -> viewModelScope.launch {
                getSchoolNoticeSubscribe().collectLatest {
                    intent {
                        reduce {
                            state.copy(subscribeSchool = it)
                        }
                        if (it) {
                            subscribeFirebaseTopic(FCM_TOPIC_SCHOOL)
                        } else {
                            unsubscribeFirebaseTopic(FCM_TOPIC_SCHOOL)
                        }
                    }
                }
            }

            SettingsSideEffect.GetDepartmentSubscribe -> viewModelScope.launch {
                getDepartmentNoticeSubscribe().collectLatest {
                    intent {
                        reduce {
                            state.copy(subscribeDepartment = it)
                        }
                        if (it) {
                            subscribeFirebaseTopic(deptList[state.userDepartment].name)
                        } else {
                            unsubscribeFirebaseTopic(deptList[state.userDepartment].name)
                        }
                    }
                }
            }

            SettingsSideEffect.GetDormSubscribe -> viewModelScope.launch {
                getDormNoticeSubscribe().collectLatest {
                    intent {
                        reduce {
                            state.copy(subscribeDormitory = it)
                        }
                        if (it) {
                            subscribeFirebaseTopic(FCM_TOPIC_DORM)
                        } else {
                            unsubscribeFirebaseTopic(FCM_TOPIC_DORM)
                        }
                    }
                }
            }

            SettingsSideEffect.DeleteAllNewArticle -> viewModelScope.launch(Dispatchers.IO) {
                deleteAllNewNoticesUseCase()
            }

            is SettingsSideEffect.SetSubscribe -> viewModelScope.launch {
                setSchoolNoticeSubscribe(sideEffect.subscribe)
                setDormNoticeSubscribe(sideEffect.subscribe)
                setDepartmentNoticeSubscribe(sideEffect.subscribe)
            }
        }
    }
}
