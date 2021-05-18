package ru.klyuv.core.di

import ru.klyuv.core.di.domain.MenuUseCaseToolsProvider
import ru.klyuv.core.di.domain.SettingsUseCaseToolsProvider

interface DomainToolsProvider: SettingsUseCaseToolsProvider,
        MenuUseCaseToolsProvider