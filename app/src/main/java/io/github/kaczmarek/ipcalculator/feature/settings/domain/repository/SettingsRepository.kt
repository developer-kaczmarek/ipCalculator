package io.github.kaczmarek.ipcalculator.feature.settings.domain.repository

import io.github.kaczmarek.ipcalculator.common.model.language.Language
import io.github.kaczmarek.ipcalculator.common.model.theme.ThemeType

interface SettingsRepository {

    fun isLanguageSelected(): Boolean

    fun getSelectedLanguage(): Language

    fun getSelectedThemeType(): ThemeType

    fun setSelectedLanguage(language: Language)

    fun setSelectedTheme(themeType: ThemeType)
}