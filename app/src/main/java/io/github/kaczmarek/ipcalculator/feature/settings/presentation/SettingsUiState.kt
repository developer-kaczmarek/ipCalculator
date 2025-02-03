package io.github.kaczmarek.ipcalculator.feature.settings.presentation

import io.github.kaczmarek.ipcalculator.common.model.language.Language
import io.github.kaczmarek.ipcalculator.common.model.theme.ThemeType

data class SettingsUiState(
    val selectedLanguage: Language = Language.English,
    val selectedThemeType: ThemeType = ThemeType.System,
)