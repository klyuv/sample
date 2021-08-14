package ru.klyuv.data.di.module.network

import dagger.Binds
import dagger.Module
import ru.klyuv.data.request.network.NetworkRequest
import ru.klyuv.data.request.network.NetworkRequestImpl
import javax.inject.Named
import javax.inject.Singleton

@Suppress("unused")
@Module
internal interface NetworkRequestModule {

    @Singleton
    @Binds
    @Named("v1")
    fun networkRequesterV1(networkRequestV1Impl: NetworkRequestImpl): NetworkRequest

}