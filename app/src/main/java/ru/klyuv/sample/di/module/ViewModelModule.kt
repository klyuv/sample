package ru.klyuv.sample.di.module

import dagger.Module
import ru.klyuv.barcode.di.BarcodeModule
import ru.klyuv.menu.di.MenuModule

@Suppress("unused")
@Module(
    includes = [
        MenuModule::class,
        BarcodeModule::class
    ]
)
interface ViewModelModule