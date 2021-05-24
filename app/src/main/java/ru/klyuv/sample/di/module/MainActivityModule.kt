package ru.klyuv.sample.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.klyuv.core.di.MainActivityScope
import ru.klyuv.sample.MainActivity

@Suppress("unused")
@Module
interface MainActivityModule {

    @MainActivityScope
    @ContributesAndroidInjector(
        modules = [FragmentModule::class]
    )
    fun mainActivityInjector(): MainActivity

}