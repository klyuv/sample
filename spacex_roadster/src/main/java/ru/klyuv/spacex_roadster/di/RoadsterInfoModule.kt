package ru.klyuv.spacex_roadster.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.klyuv.core.di.mvvm.ViewModelKey
import ru.klyuv.spacex_roadster.presentation.RoadsterInfoViewModel

@Suppress("unused")
@Module
interface RoadsterInfoModule {

    @Binds
    @IntoMap
    @ViewModelKey(RoadsterInfoViewModel::class)
    fun bindRoadsterInfoViewModel(roadsterInfoViewModel: RoadsterInfoViewModel): ViewModel

}