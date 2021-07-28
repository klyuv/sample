package ru.klyuv.sample.di.module

import dagger.Module
import ru.klyuv.barcode.di.BarcodeModule
import ru.klyuv.menu.di.MenuModule
import ru.klyuv.settings.di.SettingsModule
import ru.klyuv.spacex_roadster.di.RoadsterInfoModule

@Suppress("unused")
@Module(
    includes = [
        MenuModule::class,
        BarcodeModule::class,
        SettingsModule::class,
        RoadsterInfoModule::class
    ]
)
interface ViewModelModule