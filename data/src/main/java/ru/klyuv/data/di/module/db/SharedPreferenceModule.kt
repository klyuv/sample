package ru.klyuv.data.di.module.db

import dagger.Binds
import dagger.Module
import ru.klyuv.core.repository.SharedPreferenceRepository
import ru.klyuv.data.repository.SharedPreferenceRepositoryImpl
import javax.inject.Singleton

@Suppress("unused")
@Module(includes = [SharedPreferenceCoreModule::class])
interface SharedPreferenceModule {

    @Singleton
    @Binds
    fun provideSharedPreferenceRepository(
        sharedPreferenceRepository: SharedPreferenceRepositoryImpl
    ): SharedPreferenceRepository

}