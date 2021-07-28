package ru.klyuv.spacex_roadster.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.klyuv.core.common.ui.BaseViewModel
import ru.klyuv.core.usecase.RoadsterInfoScreenUseCase
import javax.inject.Inject

class RoadsterInfoViewModel
@Inject constructor(
    private val roadsterInfoScreenUseCase: RoadsterInfoScreenUseCase
) : BaseViewModel() {

    init {
        getData()
    }

    val roadsterFlow = roadsterInfoScreenUseCase.roadsterStateFlow

    fun getData() = viewModelScope.launch {
        roadsterInfoScreenUseCase.getRoadsterData()
    }

}