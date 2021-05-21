package ru.klyuv.core.di.data

import ru.klyuv.core.requester.ThemeRequester

interface ThemeRequesterToolsProvider {

    fun provideThemeRequester(): ThemeRequester

}