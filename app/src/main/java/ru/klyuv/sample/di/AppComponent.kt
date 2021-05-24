package ru.klyuv.sample.di

import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import ru.klyuv.barcode.di.BarcodeComponent
import ru.klyuv.core.di.DataToolsProvider
import ru.klyuv.core.di.DomainToolsProvider
import ru.klyuv.core.di.MainToolsProvider
import ru.klyuv.core.di.mvvm.ViewModelFactoryModule
import ru.klyuv.data.di.DataComponent
import ru.klyuv.domain.di.DomainComponent
import ru.klyuv.menu.di.MenuComponent
import ru.klyuv.sample.App
import ru.klyuv.sample.di.module.MainActivityModule
import ru.klyuv.sample.di.module.ViewModelModule
import ru.klyuv.settings.di.SettingsComponent
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [
        MainToolsProvider::class,
        DataToolsProvider::class,
        DomainToolsProvider::class,
        MenuComponent::class,
        BarcodeComponent::class,
        SettingsComponent::class
    ],
    modules = [
        AndroidInjectionModule::class,
        MainActivityModule::class,
        ViewModelFactoryModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {

        fun main(mainToolsProvider: MainToolsProvider): Builder

        fun data(dataToolsProvider: DataToolsProvider): Builder

        fun domain(domainToolsProvider: DomainToolsProvider): Builder

        fun menu(menuComponent: MenuComponent): Builder

        fun barcode(barcodeComponent: BarcodeComponent): Builder

        fun settings(settingsComponent: SettingsComponent): Builder

        fun build(): AppComponent

    }

    override fun inject(instance: App?)

    class Initializer private constructor() {

        companion object {
            fun init(app: App): AppComponent {
                val mainToolsProvider = MainToolsComponent.Initializer.init(app)

                val dataToolsProvider = DataComponent.Initializer.init(mainToolsProvider)

                val domainToolsProvider =
                    DomainComponent.Initializer.init(dataToolsProvider, mainToolsProvider)

                val menuComponent =
                    MenuComponent.Initializer.init(domainToolsProvider, mainToolsProvider)

                val barcodeComponent =
                    BarcodeComponent.Initializer.init(domainToolsProvider, mainToolsProvider)

                val settingsComponent =
                    SettingsComponent.Initializer.init(domainToolsProvider, mainToolsProvider)

                return DaggerAppComponent.builder()
                    .main(mainToolsProvider = mainToolsProvider)
                    .data(dataToolsProvider = dataToolsProvider)
                    .domain(domainToolsProvider = domainToolsProvider)
                    .menu(menuComponent = menuComponent)
                    .barcode(barcodeComponent = barcodeComponent)
                    .settings(settingsComponent = settingsComponent)
                    .build()
            }
        }
    }

}