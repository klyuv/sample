package ru.klyuv.stickybottom.presentation.bottom

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import ru.klyuv.stickybottom.databinding.ItemStickySimpleBinding

object StickyDelegate {

    fun init() =
        adapterDelegateViewBinding<StickyData, StickyData, ItemStickySimpleBinding>(
            { layoutInflater, parent ->
                ItemStickySimpleBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            }
        ) {
            bind {
                with(binding) {
                    tvTitle.text = item.title
                    tvValue.text = item.value
                }
            }
        }
}