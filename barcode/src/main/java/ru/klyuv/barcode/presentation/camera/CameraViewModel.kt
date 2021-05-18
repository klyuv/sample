package ru.klyuv.barcode.presentation.camera

import androidx.lifecycle.MutableLiveData
import ru.klyuv.core.common.ui.BaseViewModel
import ru.klyuv.core.model.state.CameraHolderState
import javax.inject.Inject

class CameraViewModel
@Inject constructor() : BaseViewModel() {

    /**
     * Необходима нам для переотрисовки рамки и обновления рамки обрезки,
     * если появится такая необходимость
     * Так как при первом кадре, который попадёт в анализатор будет произведён перерасчёт
     * у нас должна быть возможность перерасчёта процента обрезки
     * Перерасчёт может случить из-за того что нам не гарантируется соотношение сторон изображения
     * получаемого с камеры
     */

    val cropPercentLiveData = MutableLiveData<CameraHolderState>()

}