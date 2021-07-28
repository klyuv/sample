package ru.klyuv.core.repository

import ru.klyuv.core.common.data.NetworkFailure
import ru.klyuv.core.common.data.Result
import ru.klyuv.core.model.RoadsterInfoModel

interface SpaceXRepository {

    suspend fun getRoadsterInfo(): Result<NetworkFailure, RoadsterInfoModel>

}