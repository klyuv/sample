package ru.klyuv.stickybottom.di

import dagger.Component
import ru.klyuv.core.di.DomainToolsProvider
import ru.klyuv.core.di.MainToolsProvider


@Component(
    dependencies = [
        DomainToolsProvider::class,
        MainToolsProvider::class
    ]
)
interface StickyBottomComponent {

    class Initializer private constructor() {
        companion object {
            fun init(
                domainToolsProvider: DomainToolsProvider,
                mainToolsProvider: MainToolsProvider
            ) : StickyBottomComponent =
                DaggerStickyBottomComponent.builder()
                    .domainToolsProvider(domainToolsProvider)
                    .mainToolsProvider(mainToolsProvider)
                    .build()
        }
    }
}