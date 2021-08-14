package ru.klyuv.core.di.domain

import ru.klyuv.core.usecase.RoadsterInfoScreenUseCase

interface SpaceXUseCaseToolsProvider {

    fun provideRoadsterInfoScreenUseCase(): RoadsterInfoScreenUseCase

}