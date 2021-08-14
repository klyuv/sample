package ru.klyuv.spacex_roadster.di

import dagger.Component
import ru.klyuv.core.di.DomainToolsProvider
import ru.klyuv.core.di.MainToolsProvider


@Component(
    dependencies = [
        DomainToolsProvider::class,
        MainToolsProvider::class
    ]
)
interface RoadsterComponent {

    class Initializer private constructor() {
        companion object {
            fun init(
                domainToolsProvider: DomainToolsProvider,
                mainToolsProvider: MainToolsProvider
            ): RoadsterComponent =
                DaggerRoadsterComponent.builder()
                    .domainToolsProvider(domainToolsProvider)
                    .mainToolsProvider(mainToolsProvider)
                    .build()
        }
    }
}