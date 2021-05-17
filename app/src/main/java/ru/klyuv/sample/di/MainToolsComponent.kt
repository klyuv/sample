package ru.klyuv.sample.di

import dagger.BindsInstance
import dagger.Component
import ru.klyuv.core.App
import ru.klyuv.core.di.MainToolsProvider
import ru.klyuv.sample.di.module.AppModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface MainToolsComponent: MainToolsProvider {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun app(app: App): Builder

        fun build(): MainToolsComponent
    }

    class Initializer private constructor() {
        companion object {

            fun init(app: App): MainToolsProvider =
                DaggerMainToolsComponent.builder()
                    .app(app)
                    .build()
        }
    }
}