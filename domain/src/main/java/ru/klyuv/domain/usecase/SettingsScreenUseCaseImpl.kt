package ru.klyuv.domain.usecase

import ru.klyuv.core.requester.ThemeRequester
import ru.klyuv.core.usecase.SettingsScreenUseCase
import javax.inject.Inject

class SettingsScreenUseCaseImpl
@Inject constructor(
    private val themeRequester: ThemeRequester
) : SettingsScreenUseCase {

    override fun switchTheme() {
        themeRequester.switchTheme()
    }
}