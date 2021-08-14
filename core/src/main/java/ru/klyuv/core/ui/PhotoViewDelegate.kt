package ru.klyuv.core.ui

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import ru.klyuv.core.R
import ru.klyuv.core.common.extensions.loadImgWithCache
import ru.klyuv.core.databinding.RowPhotoBinding

object PhotoViewDelegate {

    fun init() =
        adapterDelegateViewBinding<String, String, RowPhotoBinding>(
            { layoutInflater, parent ->
                RowPhotoBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            }
        ) {
            bind {
                with(binding) {
                    ivPhoto.loadImgWithCache(
                        item,
                        R.drawable.ic_vector_delete
                    )
                }
            }
        }

}