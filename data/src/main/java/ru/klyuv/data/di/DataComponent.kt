package ru.klyuv.data.di

import dagger.Component
import ru.klyuv.core.di.DataToolsProvider
import ru.klyuv.core.di.MainToolsProvider
import ru.klyuv.data.di.module.db.SharedPreferenceModule
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [
        MainToolsProvider::class
    ],
    modules = [
        SharedPreferenceModule::class
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