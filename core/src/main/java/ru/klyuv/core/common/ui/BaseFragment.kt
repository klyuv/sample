package ru.klyuv.core.common.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerFragment
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

//    fun showFailureError(failure: Failure, action: () -> Unit = {}) {
//        when (failure) {
//            is Failure.NetworkConnection -> showAlertMessageWithoutNegativeButton(
//                getString(R.string.ERROR),
//                getString(R.string.ERROR_CHECK_INTERNET),
//                true,
//                action
//            )
//            is Failure.HTTP401 -> if (failure.error.isNotEmpty()) showLongToast(failure.error)
//            is Failure.HTTP401v2 -> if (failure.errorMessage.firstIsNotEmpty())
//                showLongToast(
//                    getString(
//                        R.string.ERROR_FAILED,
//                        failure.errorMessage.getFirstMessageWithDetail()
//                    )
//                )
//            is Failure.HTTP422 -> if (failure.errorMessage.firstIsNotEmpty())
//                showAlertMessageWithoutNegativeButton(
//                    getString(R.string.ERROR),
//                    failure.errorMessage.getFirstMessageWithDetailLn(),
//                    true,
//                    action
//                )
//            is Failure.TIMEOUT -> showLongToast(getString(R.string.ERROR_TIMEOUT))
//            is Failure.SimpleFailureRes -> showLongToast(getString(failure.error))
//            is Failure.HTTP204 -> showShortToast(getString(R.string.no_data))
//            is Failure.SimpleFailure -> showLongToast(failure.error)
//            is Failure.HTTP400 -> if (failure.errorMessage.firstIsNotEmpty())
//                showAlertMessageWithoutNegativeButton(
//                    getString(R.string.ERROR),
//                    failure.errorMessage.getFirstMessageWithDetailLn(),
//                    true,
//                    action
//                )
//            is Failure.HTTP403 -> if (failure.errorMessage.firstIsNotEmpty())
//                showAlertMessageWithoutNegativeButton(
//                    getString(R.string.ERROR),
//                    getString(R.string.ERROR_NO_ACCESS),
//                    true,
//                    action
//                )
//            is Failure.HTTP500 -> showAlertMessageWithoutNegativeButton(
//                getString(R.string.ERROR),
//                getString(R.string.ERROR_500),
//                true,
//                action
//            )
//            is Failure.SimpleFailureResWithDismissDialog -> showLongToast(getString(failure.error))
//            is Failure.SimpleFailureResWithShowAlertDialog -> showAlertMessageWithoutNegativeButton(
//                getString(R.string.ERROR),
//                getString(failure.error),
//                true,
//                action
//            )
//            else -> showLongToast(getString(R.string.ERROR_UNKNOWN))
//        }
//    }

}