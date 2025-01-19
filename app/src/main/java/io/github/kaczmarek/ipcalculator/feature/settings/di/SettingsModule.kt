package io.github.kaczmarek.ipcalculator.feature.settings.di

import io.github.kaczmarek.ipcalculator.feature.settings.data.repository.DefaultSettingsRepository
import io.github.kaczmarek.ipcalculator.feature.settings.domain.repository.SettingsRepository
import org.koin.dsl.module

val settingsModule = module {

    single<SettingsRepository> {
        DefaultSettingsRepository(
            context = get(),
        )
    }
}