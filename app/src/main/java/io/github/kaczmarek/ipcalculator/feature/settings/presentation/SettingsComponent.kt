package io.github.kaczmarek.ipcalculator.feature.settings.presentation

import io.github.kaczmarek.ipcalculator.feature.settings.domain.model.ThemeType
import io.github.kaczmarek.ipcalculator.feature.settings.domain.model.Language
import kotlinx.coroutines.flow.StateFlow

interface SettingsComponent {

    val uiState: StateFlow<SettingsUiState>

    sealed interface Output {

        data object ThemeChanged : Output
    }

    fun onLanguageItemClick(newLanguage: Language)

    fun onThemeItemClick(newTheme: ThemeType)
}