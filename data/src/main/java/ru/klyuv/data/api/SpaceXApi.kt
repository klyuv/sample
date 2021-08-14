package ru.klyuv.data.api

import retrofit2.Response
import retrofit2.http.GET
import ru.klyuv.data.model.RoadsterInfoNetworkModel

interface SpaceXApi {

    @GET(ApiUrl.ROADSTER)
    suspend fun getRoadsterInfo(): Response<RoadsterInfoNetworkModel?>

}