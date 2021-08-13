package ru.klyuv.menu.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import ru.klyuv.core.common.extensions.androidLazy
import ru.klyuv.core.common.extensions.getViewModel
import ru.klyuv.core.common.extensions.observe
import ru.klyuv.core.common.ui.BaseFragmentV2
import ru.klyuv.core.model.MainMenuItem
import ru.klyuv.core.model.MainMenuItemId
import ru.klyuv.core.model.MenuItem
import ru.klyuv.core.model.MenuSeparatorItem
import ru.klyuv.menu.databinding.FragmentMenuBinding


class MenuFragment : BaseFragmentV2<FragmentMenuBinding>() {

    private val viewModel by androidLazy { getViewModel<MenuViewModel>(viewModelFactory) }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMenuBinding = FragmentMenuBinding.inflate(layoutInflater, container, false)

    private val menuAdapter by androidLazy {
        ListDelegationAdapter<List<MenuItem>>(
            MainMenuItemDelegate.init(::onMenuClick),
            MainMenuSeparatorDelegate.init()
        )
    }

    override fun setUI(savedInstanceState: Bundle?) {
        with(viewBinding) {
            rvMenu.apply {
                adapter = menuAdapter
                layoutManager = GridLayoutManager(this.context, 3).apply {
                    spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int =
                            if (menuAdapter.items[position] is MenuSeparatorItem) 3 else 1
                    }
                }
                setHasFixedSize(true)
            }
        }
    }

    override fun observeViewModel() {
        observe(viewModel.menuLiveData, ::setMenuData)
    }

    private fun setMenuData(data: List<MenuItem>) {
        menuAdapter.items = data
        menuAdapter.notifyDataSetChanged()
    }

    private fun onMenuClick(item: MainMenuItem) {
        when (item.id) {
            MainMenuItemId.CAMERA_BARCODE -> openBarcodeListFragment()
            MainMenuItemId.SPACEX_ROADSTER -> openRoadsterInfoFragment()
            else -> {
            }
        }
    }

    private fun openBarcodeListFragment() {
        findNavController().navigate(
            MenuFragmentDirections.actionMenuFragmentToBarcodeListFragment()
        )
    }

    private fun openRoadsterInfoFragment() =
        findNavController().navigate(
            MenuFragmentDirections.actionMenuFragmentToRoadsterInfoFragment()
        )
}