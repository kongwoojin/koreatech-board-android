package com.kongjak.koreatechboard.domain.usecase.settings.subscribe

import com.kongjak.koreatechboard.domain.repository.SettingsRepository
import javax.inject.Inject

class SetSchoolNoticeSubscribe @Inject constructor(private val settingsRepository: SettingsRepository) {
    suspend operator fun invoke(subscribe: Boolean) =
        settingsRepository.setSchoolNoticeSubscribe(subscribe)
}
