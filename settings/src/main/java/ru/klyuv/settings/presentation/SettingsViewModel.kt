package ru.klyuv.settings.presentation

import ru.klyuv.core.common.ui.BaseViewModel
import ru.klyuv.core.usecase.SettingsScreenUseCase
import javax.inject.Inject

class SettingsViewModel
@Inject constructor(
    private val settingsScreenUseCase: SettingsScreenUseCase
) : BaseViewModel() {

    fun switchTheme() {
        settingsScreenUseCase.switchTheme()
    }

}