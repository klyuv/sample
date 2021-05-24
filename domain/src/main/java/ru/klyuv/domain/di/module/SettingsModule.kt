package ru.klyuv.domain.di.module

import dagger.Binds
import dagger.Module
import ru.klyuv.core.usecase.SettingsScreenUseCase
import ru.klyuv.domain.usecase.SettingsScreenUseCaseImpl

@Suppress("unused")
@Module
interface SettingsModule {

    @Binds
    fun bindSettingsScreenUseCase(settingsScreenUseCase: SettingsScreenUseCaseImpl): SettingsScreenUseCase

}