package ru.klyuv.core.di.data

import ru.klyuv.core.repository.SpaceXRepository

interface SpaceXRepositoryToolsProvider {

    fun provideSpaceXRepository(): SpaceXRepository

}