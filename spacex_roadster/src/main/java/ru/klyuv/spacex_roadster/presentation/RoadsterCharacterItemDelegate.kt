package ru.klyuv.spacex_roadster.presentation

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import ru.klyuv.core.model.RoadsterCharacterUIModel
import ru.klyuv.core.model.RoadsterUIModel
import ru.klyuv.spacex_roadster.databinding.RowRoadsterCharacterBinding

object RoadsterCharacterItemDelegate {

    fun init() =
        adapterDelegateViewBinding<RoadsterCharacterUIModel, RoadsterUIModel, RowRoadsterCharacterBinding>(
            { layoutInflater, parent ->
                RowRoadsterCharacterBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            }
        ) {
            bind {
                with(binding) {
                    tvName.text = getString(item.name)
                    tvValue.text = item.value
                }
            }
        }
}