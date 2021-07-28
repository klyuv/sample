package ru.klyuv.core.model.state

import ru.klyuv.core.common.data.MainFailureModel
import ru.klyuv.core.model.RoadsterUIModel

sealed class RoadsterState {
    object Nothing : RoadsterState()
    object Loading : RoadsterState()
    class Failed(val failure: MainFailureModel) : RoadsterState()
    class Success(val data: List<RoadsterUIModel>) : RoadsterState()
}