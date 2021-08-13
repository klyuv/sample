package ru.klyuv.core.common.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import dagger.android.support.DaggerFragment
import ru.klyuv.core.R
import ru.klyuv.core.common.data.NetworkFailure
import ru.klyuv.core.common.extensions.showAlertMessageWithoutNegativeButton
import ru.klyuv.core.common.extensions.showLongToast
import javax.inject.Inject

abstract class BaseFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutID(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI(savedInstanceState)
        observeViewModel()
    }

    @LayoutRes
    abstract fun getLayoutID(): Int

    protected abstract fun setUI(savedInstanceState: Bundle?)

    protected abstract fun observeViewModel()

    fun showFailureError(failure: NetworkFailure, action: () -> Unit = {}) {
        when (failure) {
            is NetworkFailure.NetworkConnection -> showAlertMessageWithoutNegativeButton(
                getString(R.string.ERROR),
                getString(R.string.ERROR_CHECK_INTERNET),
                true,
                action
            )
            is NetworkFailure.HTTP401 -> showLongToast(getString(R.string.ERROR_401_MESSAGE))
            is NetworkFailure.HTTP422 -> showAlertMessageWithoutNegativeButton(
                getString(R.string.ERROR),
                getString(R.string.ERROR_422_MESSAGE),
                true,
                action
            )
            is NetworkFailure.TIMEOUT -> showLongToast(getString(R.string.ERROR_TIMEOUT))
            is NetworkFailure.HTTP400 -> showAlertMessageWithoutNegativeButton(
                getString(R.string.ERROR),
                getString(R.string.ERROR_400_MESSAGE),
                true,
                action
            )
            is NetworkFailure.HTTP403 -> showAlertMessageWithoutNegativeButton(
                getString(R.string.ERROR),
                getString(R.string.ERROR_403_MESSAGE),
                true,
                action
            )
            is NetworkFailure.HTTP500 -> showAlertMessageWithoutNegativeButton(
                getString(R.string.ERROR),
                getString(R.string.ERROR_500_MESSAGE),
                true,
                action
            )
            else -> showLongToast(getString(R.string.ERROR_UNKNOWN))
        }
    }

}

abstract class BaseFragmentV2<T : ViewBinding> : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var _viewBinding: T? = null
    val viewBinding get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = getViewBinding(inflater, container)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI(savedInstanceState)
        observeViewModel()
    }

    protected abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): T

    protected abstract fun setUI(savedInstanceState: Bundle?)

    protected abstract fun observeViewModel()

    fun showFailureError(failure: NetworkFailure, action: () -> Unit = {}) {
        when (failure) {
            is NetworkFailure.NetworkConnection -> showAlertMessageWithoutNegativeButton(
                getString(R.string.ERROR),
                getString(R.string.ERROR_CHECK_INTERNET),
                true,
                action
            )
            is NetworkFailure.HTTP401 -> showLongToast(getString(R.string.ERROR_401_MESSAGE))
            is NetworkFailure.HTTP422 -> showAlertMessageWithoutNegativeButton(
                getString(R.string.ERROR),
                getString(R.string.ERROR_422_MESSAGE),
                true,
                action
            )
            is NetworkFailure.TIMEOUT -> showLongToast(getString(R.string.ERROR_TIMEOUT))
            is NetworkFailure.HTTP400 -> showAlertMessageWithoutNegativeButton(
                getString(R.string.ERROR),
                getString(R.string.ERROR_400_MESSAGE),
                true,
                action
            )
            is NetworkFailure.HTTP403 -> showAlertMessageWithoutNegativeButton(
                getString(R.string.ERROR),
                getString(R.string.ERROR_403_MESSAGE),
                true,
                action
            )
            is NetworkFailure.HTTP500 -> showAlertMessageWithoutNegativeButton(
                getString(R.string.ERROR),
                getString(R.string.ERROR_500_MESSAGE),
                true,
                action
            )
            else -> showLongToast(getString(R.string.ERROR_UNKNOWN))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }

}