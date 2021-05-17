package ru.klyuv.sample.di.module

import dagger.Module
import ru.klyuv.menu.di.MenuModule

@Suppress("unused")
@Module(
    includes = [
        MenuModule::class
    ]
)
interface ViewModelModule {
}