package com.kongjak.koreatechboard.domain.usecase.settings.theme

import com.kongjak.koreatechboard.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDarkThemeUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {
    operator fun invoke(): Flow<Int> {
        return settingsRepository.getDarkTheme()
    }
}
