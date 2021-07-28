package ru.klyuv.data.di

import dagger.Component
import ru.klyuv.core.di.DataToolsProvider
import ru.klyuv.core.di.MainToolsProvider
import ru.klyuv.data.di.module.SpaceXModule
import ru.klyuv.data.di.module.db.SharedPreferenceModule
import ru.klyuv.data.di.module.network.ApiModule
import ru.klyuv.data.di.module.network.NetworkRequestModule
import ru.klyuv.data.di.module.request.ThemeModule
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [
        MainToolsProvider::class
    ],
    modules = [
        ApiModule::class,
        NetworkRequestModule::class,
        SharedPreferenceModule::class,
        ThemeModule::class,
        SpaceXModule::class
    ]
)
interface DataComponent : DataToolsProvider {

    class Initializer private constructor() {
        companion object {

            fun init(mainToolsProvider: MainToolsProvider): DataToolsProvider =
                DaggerDataComponent.builder()
                    .mainToolsProvider(mainToolsProvider)
                    .build()
        }
    }

}