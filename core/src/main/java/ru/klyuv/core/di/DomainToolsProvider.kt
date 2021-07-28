package ru.klyuv.core.di

import ru.klyuv.core.di.domain.BarcodeUseCaseToolsProvider
import ru.klyuv.core.di.domain.MenuUseCaseToolsProvider
import ru.klyuv.core.di.domain.SettingsUseCaseToolsProvider
import ru.klyuv.core.di.domain.SpaceXUseCaseToolsProvider

interface DomainToolsProvider : SettingsUseCaseToolsProvider,
    MenuUseCaseToolsProvider,
    BarcodeUseCaseToolsProvider,
    SpaceXUseCaseToolsProvider