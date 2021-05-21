package ru.klyuv.core.requester

import ru.klyuv.core.model.CurrentTheme

interface ThemeRequester {

    fun getCurrentTheme(): CurrentTheme

    fun switchTheme()

}