package ru.klyuv.data.di.module.request

import dagger.Binds
import dagger.Module
import ru.klyuv.core.requester.ThemeRequester
import ru.klyuv.data.request.ThemeRequesterImpl

@Suppress("unused")
@Module
interface ThemeModule {

    @Binds
    fun bindThemeRequester(themeRequester: ThemeRequesterImpl): ThemeRequester

}