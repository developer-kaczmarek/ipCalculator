package io.github.kaczmarek.ipcalculator.feature.settings.presentation

import androidx.annotation.StringRes
import io.github.kaczmarek.ipcalculator.feature.settings.domain.model.ThemeType
import io.github.kaczmarek.ipcalculator.feature.settings.domain.model.Language
import androidx.compose.runtime.Immutable

@Immutable
data class SettingsUiState(
    val availableLanguages: List<LanguageItem> = emptyList(),
    val availableThemes: List<ThemeItem> = emptyList(),
) {
    class LanguageItem(
        @StringRes val titleRes: Int,
        val language: Language,
        val selected: Boolean,
    )

    class ThemeItem(
        @StringRes val titleRes: Int,
        val themeType: ThemeType,
        val selected: Boolean,
    )
}