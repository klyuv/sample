package ru.klyuv.core.di

import ru.klyuv.core.App

interface MainToolsProvider {

    fun provideContext(): App

}