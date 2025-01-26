package io.github.kaczmarek.ipcalculator.feature.settings.presentation

import io.github.kaczmarek.ipcalculator.feature.settings.domain.model.ThemeType
import io.github.kaczmarek.ipcalculator.feature.settings.domain.model.Language
import kotlinx.coroutines.flow.MutableStateFlow

class PreviewSettingsComponent : SettingsComponent {

    override val uiState = MutableStateFlow(SettingsUiState())

    override fun onLanguageItemClick(newLanguage: Language) = Unit

    override fun onThemeItemClick(newTheme: ThemeType) = Unit
}