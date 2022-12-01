package ru.klyuv.stickybottom.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import ru.klyuv.core.common.extensions.withIO
import ru.klyuv.core.common.ui.BaseViewModel
import ru.klyuv.stickybottom.presentation.bottom.StickyData
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class StickyBottomViewModel @Inject constructor() : BaseViewModel() {

    private val mutableData = MutableSharedFlow<StickyState>()
    val data: SharedFlow<StickyState> = mutableData

    init {
        viewModelScope.launch {
            withIO {
                mutableData.emit(StickyState.Progress)
                delay(2000)
                mutableData.emit(
                    StickyState.Success(
                        generateStickyData()
                    )
                )
            }
        }
    }

    private fun generateStickyData(): List<StickyData> {
        val list: MutableList<StickyData> = mutableListOf()
        for (i in 0..35) {
            list.add(
                StickyData(
                    title = "title $i",
                    value = "value $i"
                )
            )
        }
        return list
    }

}

sealed class StickyState {
    object Progress: StickyState()
    data class Success(val data: List<StickyData>): StickyState()
}