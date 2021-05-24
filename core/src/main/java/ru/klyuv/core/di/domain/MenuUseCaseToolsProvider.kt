package ru.klyuv.core.di.domain

import ru.klyuv.core.usecase.MenuScreenUseCase

interface MenuUseCaseToolsProvider {

    fun provideMenuScreenUseCase(): MenuScreenUseCase

}