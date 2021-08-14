package ru.klyuv.data.repository

import ru.klyuv.data.request.network.NetworkRequest
import javax.inject.Inject
import javax.inject.Named

abstract class BaseRepository {

    @Inject
    @Named("v1")
    lateinit var networkRequest: NetworkRequest

}