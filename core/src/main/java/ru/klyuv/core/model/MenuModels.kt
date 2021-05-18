package ru.klyuv.core.model

import androidx.annotation.DrawableRes

interface MenuItem {
    var title: String
}

class MenuSeparatorItem(override var title: String): MenuItem

class MainMenuItem(
    val id: MainMenuItemId,
    @DrawableRes val img: Int,
    override var title: String
): MenuItem

enum class MainMenuItemId {
    CAMERA_BARCODE
}