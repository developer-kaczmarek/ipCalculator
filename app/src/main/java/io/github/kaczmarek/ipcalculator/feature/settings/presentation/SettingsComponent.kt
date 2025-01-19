package io.github.kaczmarek.ipcalculator.feature.settings.presentation

import io.github.kaczmarek.ipcalculator.feature.settings.domain.model.ThemeType
import io.github.kaczmarek.ipcalculator.feature.settings.domain.model.Language
import kotlinx.coroutines.flow.StateFlow

interface SettingsComponent {

    val uiState: StateFlow<SettingsUiState>

    fun onLanguageItemClick(newLanguage: Language)

    fun onThemeItemClick(newTheme: ThemeType)
}