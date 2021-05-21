package ru.klyuv.settings.di

import dagger.Component
import ru.klyuv.core.di.DomainToolsProvider
import ru.klyuv.core.di.MainToolsProvider

@Component(
    dependencies = [
        DomainToolsProvider::class,
        MainToolsProvider::class
    ]
)
interface SettingsComponent {

    class Initializer private constructor() {
        companion object {
            fun init(
                domainToolsProvider: DomainToolsProvider,
                mainToolsProvider: MainToolsProvider
            ): SettingsComponent =
                DaggerSettingsComponent.builder()
                    .domainToolsProvider(domainToolsProvider)
                    .mainToolsProvider(mainToolsProvider)
                    .build()
        }
    }

}