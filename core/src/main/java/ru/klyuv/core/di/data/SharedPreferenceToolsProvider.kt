package ru.klyuv.core.di.data

import ru.klyuv.core.repository.SharedPreferenceRepository

interface SharedPreferenceToolsProvider {

    fun provideSharedPreferenceRepository(): SharedPreferenceRepository

}