package ru.klyuv.core.di.domain

import ru.klyuv.core.usecase.BarcodeScreenUseCase

interface BarcodeUseCaseToolsProvider {

    fun provideBarcodeScreenUseCase(): BarcodeScreenUseCase

}