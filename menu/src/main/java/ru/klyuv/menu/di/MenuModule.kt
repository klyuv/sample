package ru.klyuv.menu.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.klyuv.core.di.mvvm.ViewModelKey
import ru.klyuv.menu.presentation.MenuViewModel

@Suppress("unused")
@Module
interface MenuModule {

    @Binds
    @IntoMap
    @ViewModelKey(MenuViewModel::class)
    fun bindMenuViewModel(menuViewModel: MenuViewModel): ViewModel

}