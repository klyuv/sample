package ru.klyuv.core.di.domain

import ru.klyuv.core.usecase.SettingsScreenUseCase

interface SettingsUseCaseToolsProvider {

    fun provideSettingsScreenUseCase(): SettingsScreenUseCase

}