package ru.klyuv.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.klyuv.core.common.extensions.withIO
import ru.klyuv.core.model.MainMenuItem
import ru.klyuv.core.model.MainMenuItemId
import ru.klyuv.core.model.MenuItem
import ru.klyuv.core.model.MenuSeparatorItem
import ru.klyuv.core.usecase.MenuScreenUseCase
import ru.klyuv.domain.R
import javax.inject.Inject

class MenuScreenUseCaseImpl
@Inject constructor() : MenuScreenUseCase {

    private val menuMutableLiveData = MutableLiveData<List<MenuItem>>()

    override val menuLiveData: LiveData<List<MenuItem>>
        get() = menuMutableLiveData

    override suspend fun initMenu() = withIO {
        val menu: ArrayList<MenuItem> = arrayListOf()
        menu.add(MenuSeparatorItem(R.string.camera))
        menu.add(
            MainMenuItem(
                MainMenuItemId.CAMERA_BARCODE,
                R.drawable.ic_vector_barcode,
                R.string.MENU_CAMERA
            )
        )
        menu.add(MenuSeparatorItem(R.string.SpaceX))

        menu.add(
            MainMenuItem(
                MainMenuItemId.SPACEX_ROADSTER,
                R.drawable.ic_vector_cabriolet,
                R.string.MENU_SPACEX_ROADSTER
            )
        )

        postMenuData(menu)
    }

    private fun postMenuData(data: List<MenuItem>) {
        menuMutableLiveData.postValue(data)
    }
}