package ru.klyuv.domain.usecase

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.klyuv.core.App
import ru.klyuv.core.common.data.MainFailureModel
import ru.klyuv.core.common.data.NetworkFailure
import ru.klyuv.core.common.data.map
import ru.klyuv.core.common.extensions.withIO
import ru.klyuv.core.model.RoadsterUIModel
import ru.klyuv.core.model.state.RoadsterState
import ru.klyuv.core.repository.SpaceXRepository
import ru.klyuv.core.usecase.RoadsterInfoScreenUseCase
import javax.inject.Inject

class RoadsterInfoScreenUseCaseImpl
@Inject constructor(
    private val spaceXRepository: SpaceXRepository,
    private val app: App
) : RoadsterInfoScreenUseCase {

    private val context = app.getApplicationContext()

    private val roadsterMutableStateFlow = MutableStateFlow<RoadsterState>(RoadsterState.Nothing)
    override val roadsterStateFlow: StateFlow<RoadsterState>
        get() = roadsterMutableStateFlow

    override suspend fun getRoadsterData() = withIO {
        postLoadingState()
        spaceXRepository.getRoadsterInfo()
            .map { it.mapToRoadsterUIList(context) }
            .either(::postFailureState, ::postSuccessState)
    }

    private suspend fun postLoadingState() {
        roadsterMutableStateFlow.emit(RoadsterState.Loading)
    }

    private fun postFailureState(failure: NetworkFailure) {
        roadsterMutableStateFlow.value = RoadsterState.Failed(MainFailureModel(failure))
    }

    private fun postSuccessState(data: List<RoadsterUIModel>) {
        roadsterMutableStateFlow.value = RoadsterState.Success(data)
    }
}