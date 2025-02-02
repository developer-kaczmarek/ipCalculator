package io.github.kaczmarek.ipcalculator.feature.settings.di

import io.github.kaczmarek.ipcalculator.feature.settings.data.repository.DefaultSettingsRepository
import io.github.kaczmarek.ipcalculator.feature.settings.data.source.SettingsLocalDataStore
import io.github.kaczmarek.ipcalculator.feature.settings.domain.repository.SettingsRepository
import io.github.kaczmarek.ipcalculator.feature.settings.presentation.LanguageManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val settingsModule = module {

    single { SettingsLocalDataStore(context = androidContext()) }

    single<SettingsRepository> {
        DefaultSettingsRepository(
            settingsLocalDataStore = get(),
        )
    }

    single { LanguageManager(context = androidContext()) }
}