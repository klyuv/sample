package ru.klyuv.spacex_roadster.presentation

import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import ru.klyuv.core.common.extensions.toVisibleOrGone
import ru.klyuv.core.model.RoadsterMainInfoUIModel
import ru.klyuv.core.model.RoadsterUIModel
import ru.klyuv.core.ui.PhotoViewDelegate
import ru.klyuv.spacex_roadster.databinding.RowRoadsterMainBinding

object RoadsterMainItemDelegate {

    fun init(
        youTubeClick: (String) -> Unit,
        wikipediaClick: (String) -> Unit
    ) =
        adapterDelegateViewBinding<RoadsterMainInfoUIModel, RoadsterUIModel, RowRoadsterMainBinding>(
            { layoutInflater, parent ->
                RowRoadsterMainBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            }
        ) {
            bind {
                val photoAdapter = ListDelegationAdapter<List<String>>(
                    PhotoViewDelegate.init()
                )
                with(binding) {
                    vpPhotos.apply {
                        adapter = photoAdapter
                        currentItem = 0
                    }
                    photoAdapter.items = item.flickrImages
                    photoAdapter.notifyDataSetChanged()
                    ivYouTube.toVisibleOrGone(item.videoUrl.isNotEmpty())
                    ivYouTube.setOnClickListener {
                        youTubeClick(item.videoUrl)
                    }
                    ivWikipedia.toVisibleOrGone(item.wikipediaUrl.isNotEmpty())
                    ivWikipedia.setOnClickListener {
                        wikipediaClick(item.wikipediaUrl)
                    }
                    tvName.text = item.name
                    tvDetails.text = item.details
                }
            }
        }

}