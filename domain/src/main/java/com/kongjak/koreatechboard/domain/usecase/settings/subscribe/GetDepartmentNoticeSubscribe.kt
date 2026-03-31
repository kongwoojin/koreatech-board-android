package com.kongjak.koreatechboard.domain.usecase.settings.subscribe

import com.kongjak.koreatechboard.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDepartmentNoticeSubscribe @Inject constructor(private val settingsRepository: SettingsRepository) {
    operator fun invoke(): Flow<Boolean> = settingsRepository.getDepartmentNoticeSubscribe()
}
