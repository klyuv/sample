package ru.klyuv.data.di.module

import dagger.Binds
import dagger.Module
import ru.klyuv.core.repository.SpaceXRepository
import ru.klyuv.data.repository.SpaceXRepositoryImpl

@Suppress("unused")
@Module
interface SpaceXModule {

    @Binds
    fun bindSpaceXRepository(spaceXRepository: SpaceXRepositoryImpl): SpaceXRepository

}