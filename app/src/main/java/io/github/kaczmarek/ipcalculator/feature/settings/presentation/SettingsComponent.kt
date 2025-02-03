package io.github.kaczmarek.ipcalculator.feature.settings.presentation

import io.github.kaczmarek.ipcalculator.common.model.theme.ThemeType
import io.github.kaczmarek.ipcalculator.common.model.language.Language
import kotlinx.coroutines.flow.StateFlow

interface SettingsComponent {

    val uiState: StateFlow<SettingsUiState>

    sealed interface Output {

        data object ThemeChanged : Output
    }

    fun onLanguageItemClick(newLanguage: Language)

    fun onThemeItemClick(newTheme: ThemeType)
}