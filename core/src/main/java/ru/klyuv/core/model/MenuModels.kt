package ru.klyuv.core.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

interface MenuItem {
    var title: Int
}

class MenuSeparatorItem(@StringRes override var title: Int) : MenuItem

class MainMenuItem(
    val id: MainMenuItemId,
    @DrawableRes val img: Int,
    @StringRes override var title: Int
) : MenuItem

enum class MainMenuItemId {
    CAMERA_BARCODE,
    SPACEX_ROADSTER
}