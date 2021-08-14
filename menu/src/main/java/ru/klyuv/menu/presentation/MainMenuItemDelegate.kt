package ru.klyuv.menu.presentation

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import ru.klyuv.core.common.extensions.setOnSingleClickListener
import ru.klyuv.core.model.MainMenuItem
import ru.klyuv.core.model.MenuItem
import ru.klyuv.menu.databinding.RowMainMenuItemBinding


object MainMenuItemDelegate {

    fun init(
        menuItemClick: (MainMenuItem) -> Unit,
    ) = adapterDelegateViewBinding<MainMenuItem, MenuItem, RowMainMenuItemBinding>(
        { layoutInflater, parent ->
            RowMainMenuItemBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        }
    ) {
        bind {
            binding.llItemContainer.setOnSingleClickListener { menuItemClick(item) }
            with(binding) {
                tvTitle.text = getString(item.title)
                ivItem.setImageResource(item.img)

            }
        }
    }
}