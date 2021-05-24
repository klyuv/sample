package ru.klyuv.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.klyuv.core.App
import ru.klyuv.core.common.extensions.withIO
import ru.klyuv.core.model.MainMenuItem
import ru.klyuv.core.model.MainMenuItemId
import ru.klyuv.core.model.MenuItem
import ru.klyuv.core.usecase.MenuScreenUseCase
import ru.klyuv.domain.R
import javax.inject.Inject

class MenuScreenUseCaseImpl
@Inject constructor(
    private val app: App
) : MenuScreenUseCase {

    private val menuMutableLiveData = MutableLiveData<List<MenuItem>>()

    private val context = app.getApplicationContext()

    override val menuLiveData: LiveData<List<MenuItem>>
        get() = menuMutableLiveData

    override suspend fun initMenu() = withIO {
        val menu: ArrayList<MenuItem> = arrayListOf()
        menu.add(
            MainMenuItem(
                MainMenuItemId.CAMERA_BARCODE,
                R.drawable.ic_vector_barcode,
                context.getString(R.string.MENU_CAMERA)
            )
        )

        postMenuData(menu)
    }

    private fun postMenuData(data: List<MenuItem>) {
        menuMutableLiveData.postValue(data)
    }
}