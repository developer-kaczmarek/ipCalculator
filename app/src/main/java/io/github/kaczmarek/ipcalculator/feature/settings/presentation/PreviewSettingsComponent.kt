package io.github.kaczmarek.ipcalculator.feature.settings.presentation

import io.github.kaczmarek.ipcalculator.common.model.theme.ThemeType
import io.github.kaczmarek.ipcalculator.common.model.language.Language
import kotlinx.coroutines.flow.MutableStateFlow

class PreviewSettingsComponent : SettingsComponent {

    override val uiState = MutableStateFlow(SettingsUiState())

    override fun onLanguageItemClick(newLanguage: Language) = Unit

    override fun onThemeItemClick(newTheme: ThemeType) = Unit
}