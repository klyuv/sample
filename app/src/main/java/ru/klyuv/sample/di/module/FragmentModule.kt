package ru.klyuv.sample.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.klyuv.barcode.presentation.BarcodeListFragment
import ru.klyuv.barcode.presentation.camera.CameraFragment
import ru.klyuv.core.di.FragmentScope
import ru.klyuv.menu.presentation.MenuFragment

@Suppress("unused")
@Module
interface FragmentModule {

    @FragmentScope
    @ContributesAndroidInjector
    fun contributeMenuFragment(): MenuFragment

    @FragmentScope
    @ContributesAndroidInjector
    fun contributeCameraFragment(): CameraFragment

    @FragmentScope
    @ContributesAndroidInjector
    fun contributeBarcodeListFragment(): BarcodeListFragment
}