package ru.klyuv.settings.presentation

import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.klyuv.core.common.extensions.androidLazy
import ru.klyuv.core.common.extensions.getViewModel
import ru.klyuv.core.common.ui.BaseFragment
import ru.klyuv.settings.R
import ru.klyuv.settings.databinding.FragmentSettingsBinding


class SettingsFragment : BaseFragment() {

    private val viewModel by androidLazy { getViewModel<SettingsViewModel>(viewModelFactory) }
    private val viewBinding: FragmentSettingsBinding by viewBinding(FragmentSettingsBinding::bind)

    override fun getLayoutID(): Int = R.layout.fragment_settings

    override fun setUI(savedInstanceState: Bundle?) {
        viewBinding.container.setOnClickListener {
            viewModel.switchTheme()
        }
    }

    override fun observeViewModel() {

    }


}