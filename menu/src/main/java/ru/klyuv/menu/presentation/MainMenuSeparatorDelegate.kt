package ru.klyuv.menu.presentation

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import ru.klyuv.core.model.MenuItem
import ru.klyuv.core.model.MenuSeparatorItem
import ru.klyuv.menu.databinding.RowMainMenuSeparatorBinding


object MainMenuSeparatorDelegate {

    fun init() =
        adapterDelegateViewBinding<MenuSeparatorItem, MenuItem, RowMainMenuSeparatorBinding>(
            { layoutInflater, parent ->
                RowMainMenuSeparatorBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            }
        ) {
            bind {
                with(binding) {
                    tvTitle.text = getString(item.title)
                }
            }
        }

}