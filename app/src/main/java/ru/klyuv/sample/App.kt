package ru.klyuv.sample

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import ru.klyuv.core.App
import ru.klyuv.sample.di.AppComponent

class App : DaggerApplication(), App {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = appComponent

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        initAppComponent()
        super.onCreate()
    }

    private fun initAppComponent() {
        appComponent = createAppComponent()
    }

    private fun createAppComponent() = AppComponent.Initializer.init(this)

}