package com.kongjak.koreatechboard.domain.usecase.settings.theme

import com.kongjak.koreatechboard.domain.repository.SettingsRepository
import javax.inject.Inject

class SetDynamicThemeUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {
    suspend operator fun invoke(state: Boolean) {
        settingsRepository.setDynamicTheme(state)
    }
}
