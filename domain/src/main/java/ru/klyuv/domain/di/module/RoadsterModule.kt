package ru.klyuv.domain.di.module

import dagger.Binds
import dagger.Module
import ru.klyuv.core.usecase.RoadsterInfoScreenUseCase
import ru.klyuv.domain.usecase.RoadsterInfoScreenUseCaseImpl

@Suppress("unused")
@Module
interface RoadsterModule {

    @Binds
    fun bindsRoadsterInfoScreenUseCase(roadsterInfoScreenUseCase: RoadsterInfoScreenUseCaseImpl): RoadsterInfoScreenUseCase

}