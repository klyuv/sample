package ru.klyuv.data.di.module.network

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.klyuv.data.api.SpaceXApi
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [RetrofitModule::class])
class ApiModule {

    @Provides
    @Singleton
    fun provideSpaceXApi(@Named("SpaceXApi") retrofit: Retrofit): SpaceXApi =
        retrofit.create(SpaceXApi::class.java)

}