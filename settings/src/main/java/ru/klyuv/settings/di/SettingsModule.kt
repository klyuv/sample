package ru.klyuv.settings.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.klyuv.core.di.mvvm.ViewModelKey
import ru.klyuv.settings.presentation.SettingsViewModel

@Suppress("unused")
@Module
interface SettingsModule {

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    fun bindSettingsViewModel(settingsViewModel: SettingsViewModel): ViewModel

}