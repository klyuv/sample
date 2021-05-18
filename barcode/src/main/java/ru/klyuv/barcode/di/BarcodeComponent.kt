package ru.klyuv.barcode.di

import dagger.Component
import ru.klyuv.core.di.DomainToolsProvider
import ru.klyuv.core.di.MainToolsProvider

@Component(
    dependencies = [
        DomainToolsProvider::class,
        MainToolsProvider::class
    ]
)
interface BarcodeComponent {

    class Initializer private constructor() {
        companion object {
            fun init(
                domainToolsProvider: DomainToolsProvider,
                mainToolsProvider: MainToolsProvider
            ): BarcodeComponent =
                DaggerBarcodeComponent.builder()
                    .domainToolsProvider(domainToolsProvider)
                    .mainToolsProvider(mainToolsProvider)
                    .build()
        }
    }

}