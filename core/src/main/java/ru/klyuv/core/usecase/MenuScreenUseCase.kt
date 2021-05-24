package ru.klyuv.core.usecase

import androidx.lifecycle.LiveData
import ru.klyuv.core.model.MenuItem

interface MenuScreenUseCase {

    val menuLiveData: LiveData<List<MenuItem>>

    suspend fun initMenu()

}