package io.github.kaczmarek.ipcalculator.feature.settings.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import io.github.kaczmarek.ipcalculator.feature.settings.domain.model.ThemeType
import io.github.kaczmarek.ipcalculator.feature.settings.domain.model.Language
import io.github.kaczmarek.ipcalculator.feature.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultSettingsRepository(
    private val context: Context,
): SettingsRepository {

    private val Context.dataStore by preferencesDataStore("app_preferences")
    private val languageKey = stringPreferencesKey("preference_language_key")
    private val themeKey = stringPreferencesKey("preference_theme_key")

    override val selectedLanguageFlow: Flow<Language> = context.dataStore.data
        .map { preferences ->
            Language.entries.find { it.code ==  preferences[languageKey] } ?: Language.English
        }

    override val selectedThemeFlow: Flow<ThemeType> = context.dataStore.data
        .map { preferences ->
            ThemeType.entries.find { it.name ==  preferences[themeKey] } ?: ThemeType.System
        }

    override suspend fun setSelectedLanguage(language: Language) {
        context.dataStore.edit { prefs ->
            prefs[languageKey] = language.code
        }
    }

    override suspend fun setSelectedTheme(themeType: ThemeType) {
        context.dataStore.edit { prefs ->
            prefs[themeKey] = themeType.name
        }
    }
}