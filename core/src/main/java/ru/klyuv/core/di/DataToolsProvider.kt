package ru.klyuv.core.di

import ru.klyuv.core.di.data.SharedPreferenceToolsProvider
import ru.klyuv.core.di.data.SpaceXRepositoryToolsProvider
import ru.klyuv.core.di.data.ThemeRequesterToolsProvider

interface DataToolsProvider : SharedPreferenceToolsProvider,
    ThemeRequesterToolsProvider,
    SpaceXRepositoryToolsProvider