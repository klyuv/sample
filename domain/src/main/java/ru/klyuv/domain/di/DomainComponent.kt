package ru.klyuv.domain.di

import dagger.Component
import ru.klyuv.core.di.DataToolsProvider
import ru.klyuv.core.di.DomainToolsProvider
import ru.klyuv.core.di.MainToolsProvider
import ru.klyuv.domain.di.module.BarcodeModule
import ru.klyuv.domain.di.module.MenuModule
import ru.klyuv.domain.di.module.SettingsModule
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [
        DataToolsProvider::class,
        MainToolsProvider::class
    ],
    modules = [
        SettingsModule::class,
        MenuModule::class,
        BarcodeModule::class
    ]
)
interface DomainComponent : DomainToolsProvider {

    class Initializer private constructor() {

        companion object {

            fun init(
                dataToolsProvider: DataToolsProvider,
                mainToolsProvider: MainToolsProvider
            ): DomainToolsProvider =
                DaggerDomainComponent.builder()
                    .dataToolsProvider(dataToolsProvider)
                    .mainToolsProvider(mainToolsProvider)
                    .build()
        }
    }

}