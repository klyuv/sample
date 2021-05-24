package ru.klyuv.sample.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.klyuv.sample.App
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(app: App): Context = app

}