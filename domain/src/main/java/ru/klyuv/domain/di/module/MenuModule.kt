package ru.klyuv.domain.di.module

import dagger.Binds
import dagger.Module
import ru.klyuv.core.usecase.MenuScreenUseCase
import ru.klyuv.domain.usecase.MenuScreenUseCaseImpl

@Suppress("unused")
@Module
interface MenuModule {

    @Binds
    fun bindMenuScreenUseCase(menuScreenUseCase: MenuScreenUseCaseImpl): MenuScreenUseCase

}