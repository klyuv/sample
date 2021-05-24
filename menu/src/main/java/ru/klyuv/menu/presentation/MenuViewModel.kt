package ru.klyuv.menu.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.klyuv.core.common.ui.BaseViewModel
import ru.klyuv.core.usecase.MenuScreenUseCase
import javax.inject.Inject

class MenuViewModel
@Inject constructor(
    private val menuScreenUseCase: MenuScreenUseCase
): BaseViewModel() {

    init {
        viewModelScope.launch {
            menuScreenUseCase.initMenu()
        }
    }

    val menuLiveData = menuScreenUseCase.menuLiveData

}