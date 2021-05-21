package ru.klyuv.domain.di.module

import dagger.Binds
import dagger.Module
import ru.klyuv.core.usecase.BarcodeScreenUseCase
import ru.klyuv.domain.usecase.BarcodeScreenUseCaseImpl

@Suppress("unused")
@Module
interface BarcodeModule {

    @Binds
    fun bindBarcodeScreenUseCase(barcodeScreenUseCase: BarcodeScreenUseCaseImpl): BarcodeScreenUseCase

}