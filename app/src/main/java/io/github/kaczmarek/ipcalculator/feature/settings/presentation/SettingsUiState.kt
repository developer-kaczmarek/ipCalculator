package io.github.kaczmarek.ipcalculator.feature.settings.presentation

import io.github.kaczmarek.ipcalculator.feature.settings.domain.model.Language
import io.github.kaczmarek.ipcalculator.feature.settings.domain.model.ThemeType


data class SettingsUiState(
    val selectedLanguage: Language = Language.English,
    val selectedThemeType: ThemeType = ThemeType.System,
)