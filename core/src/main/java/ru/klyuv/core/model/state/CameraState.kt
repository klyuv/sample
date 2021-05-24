package ru.klyuv.core.model.state

import androidx.annotation.IntRange

sealed class CameraHolderState {
    object Failure : CameraHolderState()
    object WithoutHolder: CameraHolderState()
    class HolderDraw(
        @IntRange(from = 0, to = 99) val first: Int,
        @IntRange(from = 0, to = 99) val second: Int
    ): CameraHolderState()
}