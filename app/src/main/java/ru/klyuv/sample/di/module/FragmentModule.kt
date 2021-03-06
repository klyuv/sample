package ru.klyuv.sample.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.klyuv.barcode.presentation.BarcodeListFragment
import ru.klyuv.barcode.presentation.camera.CameraFragment
import ru.klyuv.core.di.FragmentScope
import ru.klyuv.menu.presentation.MenuFragment
import ru.klyuv.settings.presentation.SettingsFragment
import ru.klyuv.spacex_roadster.presentation.RoadsterInfoFragment

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

    @FragmentScope
    @ContributesAndroidInjector
    fun contributeSettingsFragment(): SettingsFragment

    @FragmentScope
    @ContributesAndroidInjector
    fun contributeRoadsterInfoFragment(): RoadsterInfoFragment

}