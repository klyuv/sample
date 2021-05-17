package ru.klyuv.sample.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.klyuv.core.di.MainActivityScope
import ru.klyuv.sample.MainActivity

@Suppress("unused")
@Module
interface MainActivityModule {

    @MainActivityScope
    @ContributesAndroidInjector
    fun mainActivityInjector(): MainActivity

}