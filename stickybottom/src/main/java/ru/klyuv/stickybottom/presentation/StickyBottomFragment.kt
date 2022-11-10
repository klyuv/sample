package ru.klyuv.stickybottom.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.klyuv.core.common.extensions.androidLazy
import ru.klyuv.core.common.extensions.getViewModel
import ru.klyuv.core.common.ui.BaseFragmentV2
import ru.klyuv.stickybottom.databinding.FragmentStickyBottomBinding

class StickyBottomFragment: BaseFragmentV2<FragmentStickyBottomBinding>() {

    private val viewModel by androidLazy { getViewModel<StickyBottomViewModel>(viewModelFactory) }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentStickyBottomBinding = FragmentStickyBottomBinding.inflate(layoutInflater, container, false)

    override fun setUI(savedInstanceState: Bundle?) {
        Unit
    }

    override fun observeViewModel() {
        Unit
    }


}