package ru.klyuv.stickybottom.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.klyuv.core.di.mvvm.ViewModelKey
import ru.klyuv.stickybottom.presentation.StickyBottomViewModel


@Suppress("unused")
@Module
interface StickyBottomModule {

    @Binds
    @IntoMap
    @ViewModelKey(StickyBottomViewModel::class)
    fun bindStickyViewModel(stickyBottomViewModel: StickyBottomViewModel): ViewModel

}