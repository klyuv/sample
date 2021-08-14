package ru.klyuv.data.repository

import ru.klyuv.core.common.data.NetworkFailure
import ru.klyuv.core.common.data.Result
import ru.klyuv.core.model.RoadsterInfoModel
import ru.klyuv.core.repository.SpaceXRepository
import ru.klyuv.data.api.SpaceXApi
import ru.klyuv.data.mapper.mapToRoadsterInfoModel
import javax.inject.Inject

class SpaceXRepositoryImpl
@Inject constructor(
    private val spaceXApi: SpaceXApi
) : BaseRepository(), SpaceXRepository {

    override suspend fun getRoadsterInfo(): Result<NetworkFailure, RoadsterInfoModel> =
        networkRequest.make(
            { spaceXApi.getRoadsterInfo() },
            { response -> response.mapToRoadsterInfoModel() }
        )

}