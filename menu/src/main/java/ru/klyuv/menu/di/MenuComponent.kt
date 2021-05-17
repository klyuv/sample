package ru.klyuv.menu.di

import dagger.Component
import ru.klyuv.core.di.DomainToolsProvider
import ru.klyuv.core.di.MainToolsProvider

@Component(
    dependencies = [
        DomainToolsProvider::class,
        MainToolsProvider::class
    ]
)
interface MenuComponent {

    class Initializer private constructor() {
        companion object {
            fun init(
                domainToolsProvider: DomainToolsProvider,
                mainToolsProvider: MainToolsProvider
            ): MenuComponent =
                DaggerMenuComponent.builder()
                    .domainToolsProvider(domainToolsProvider)
                    .mainToolsProvider(mainToolsProvider)
                    .build()
        }
    }

}