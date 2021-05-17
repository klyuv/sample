package ru.klyuv.data.di.module.db

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import ru.klyuv.core.App
import javax.inject.Singleton

@Module
class SharedPreferenceCoreModule {

    private val PREF_NAME = "klyuv_sample"

    @Provides
    @Singleton
    fun provideSharedPreferences(app: App): SharedPreferences =
        app.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
}