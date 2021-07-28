package ru.klyuv.core.usecase

import kotlinx.coroutines.flow.StateFlow
import ru.klyuv.core.model.state.RoadsterState

interface RoadsterInfoScreenUseCase {

    val roadsterStateFlow: StateFlow<RoadsterState>

    suspend fun getRoadsterData()

}