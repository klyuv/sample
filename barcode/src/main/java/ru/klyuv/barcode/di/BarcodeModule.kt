package ru.klyuv.barcode.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.klyuv.barcode.presentation.BarcodeListViewModel
import ru.klyuv.barcode.presentation.camera.CameraViewModel
import ru.klyuv.core.di.mvvm.ViewModelKey

@Suppress("unused")
@Module
interface BarcodeModule {

    @Binds
    @IntoMap
    @ViewModelKey(CameraViewModel::class)
    fun bindCameraViewModel(cameraViewModel: CameraViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BarcodeListViewModel::class)
    fun bindBarCodeListViewModel(barcodeListViewModel: BarcodeListViewModel): ViewModel

}