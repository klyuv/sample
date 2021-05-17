package ru.klyuv.sample.di

import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import ru.klyuv.sample.App
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [],
    modules = [
        AndroidInjectionModule::class,
        MainActivityModule::class
    ]
)
interface AppComponent: AndroidInjector<App> {

    @Component.Builder
    interface Builder {

        fun build(): AppComponent

    }

    override fun inject(instance: App?)

    class Initializer private constructor() {

        companion object {
            fun init (app: App): AppComponent {
                return DaggerAppComponent.builder().build()
            }
        }
    }

}