package io.github.kaczmarek.ipcalculator.feature.settings.domain.repository

import io.github.kaczmarek.ipcalculator.feature.settings.domain.model.ThemeType
import io.github.kaczmarek.ipcalculator.feature.settings.domain.model.Language
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    val selectedLanguageFlow: Flow<Language>

    val selectedThemeFlow: Flow<ThemeType>

    suspend fun setSelectedLanguage(language: Language)

    suspend fun setSelectedTheme(themeType: ThemeType)
}