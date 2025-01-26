package io.github.kaczmarek.ipcalculator.feature.settings.presentation

import com.arkivanov.decompose.ComponentContext
import io.github.kaczmarek.ipcalculator.feature.settings.domain.repository.SettingsRepository
import io.github.kaczmarek.ipcalculator.common.utils.componentCoroutineScope
import io.github.kaczmarek.ipcalculator.feature.settings.domain.model.ThemeType
import io.github.kaczmarek.ipcalculator.feature.settings.domain.model.Language
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import io.github.kaczmarek.ipcalculator.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultSettingsComponent(
    componentContext: ComponentContext,
) : ComponentContext by componentContext, SettingsComponent, KoinComponent {

    override val uiState = MutableStateFlow(SettingsUiState())

    private val settingsRepository: SettingsRepository by inject()

    private val coroutineScope = componentCoroutineScope()

    init {
        prepareUiState()
    }

    override fun onLanguageItemClick(newLanguage: Language) {
        coroutineScope.launch {
            settingsRepository.setSelectedLanguage(newLanguage)
            uiState.update { uiState.value.copy(availableLanguages = getLanguages(newLanguage)) }
        }
    }

    override fun onThemeItemClick(newTheme: ThemeType) {
        coroutineScope.launch {
            settingsRepository.setSelectedTheme(newTheme)
            uiState.update { uiState.value.copy(availableThemes = getThemes(newTheme)) }
        }
    }

    private fun prepareUiState() {
        coroutineScope.launch {
            val selectedLanguage = settingsRepository.selectedLanguageFlow.first()
            val selectedTheme = settingsRepository.selectedThemeFlow.first()

            uiState.update {
                uiState.value.copy(
                    availableLanguages = getLanguages(selectedLanguage),
                    availableThemes = getThemes(selectedTheme),
                )
            }
        }
    }

    private suspend fun getLanguages(selectedLanguage: Language): List<SettingsUiState.LanguageItem> {
        return withContext(Dispatchers.Default) {
            listOf(
                SettingsUiState.LanguageItem(
                    titleRes = R.string.settings_locale_en,
                    language = Language.English,
                    selected = Language.English == selectedLanguage,
                ),
                SettingsUiState.LanguageItem(
                    titleRes = R.string.settings_locale_ru,
                    language = Language.Russian,
                    selected = Language.Russian == selectedLanguage,
                ),
                SettingsUiState.LanguageItem(
                    titleRes = R.string.settings_locale_kk,
                    language = Language.Kazakh,
                    selected = Language.Kazakh == selectedLanguage,
                )
            )
        }
    }

    private suspend fun getThemes(selectedTheme: ThemeType): List<SettingsUiState.ThemeItem> {
        return withContext(Dispatchers.Default) {
            listOf(
                SettingsUiState.ThemeItem(
                    titleRes = R.string.settings_theme_system,
                    themeType = ThemeType.System,
                    selected = ThemeType.System == selectedTheme,
                ),
                SettingsUiState.ThemeItem(
                    titleRes = R.string.settings_theme_dark,
                    themeType = ThemeType.Dark,
                    selected = ThemeType.Dark == selectedTheme,
                ),
                SettingsUiState.ThemeItem(
                    titleRes = R.string.settings_theme_light,
                    themeType = ThemeType.Light,
                    selected = ThemeType.Light == selectedTheme,
                ),
            )
        }
    }
}