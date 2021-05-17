package ru.klyuv.menu.presentation

import android.os.Bundle
import ru.klyuv.core.common.ui.BaseFragment
import ru.klyuv.menu.R


class MenuFragment: BaseFragment() {

    override fun getLayoutID(): Int = R.layout.fragment_menu

    override fun setUI(savedInstanceState: Bundle?) {
        Unit
    }

    override fun observeViewModel() {
        Unit
    }
}