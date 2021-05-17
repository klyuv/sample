package ru.klyuv.core.common.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerAppCompatDialogFragment
import ru.klyuv.core.R
import javax.inject.Inject

abstract class BaseDialogFullScreenFragment : DaggerAppCompatDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(getLayoutID(), null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            DialogFragment.STYLE_NORMAL,
            R.style.AppTheme_FullScreenDialog
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI(savedInstanceState)
        observeViewModel()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    @LayoutRes
    abstract fun getLayoutID(): Int

    protected abstract fun setUI(savedInstanceState: Bundle?)

    protected abstract fun observeViewModel()

//    fun showFailureError(failure: Failure) {
//        when (failure) {
//            is Failure.NetworkConnection -> showLongToast(getString(ru.wildberries.core.R.string.ERROR_CHECK_INTERNET))
//            is Failure.HTTP401 -> if (failure.error.isNotEmpty()) showLongToast(failure.error)
//            is Failure.HTTP401v2 -> if (failure.errorMessage.firstIsNotEmpty())
//                showLongToast(
//                    getString(
//                        ru.wildberries.core.R.string.ERROR_FAILED,
//                        failure.errorMessage.getFirstMessageWithDetail()
//                    )
//                )
//            is Failure.HTTP422 -> if (failure.errorMessage.firstIsNotEmpty())
//                showAlertMessageWithoutNegativeButton(
//                    getString(ru.wildberries.core.R.string.ERROR),
//                    failure.errorMessage.getFirstMessageWithDetailLn(),
//                    true
//                )
//            is Failure.TIMEOUT -> showLongToast(getString(R.string.ERROR_TIMEOUT))
//            is Failure.SimpleFailureRes -> showLongToast(getString(failure.error))
//            is Failure.HTTP204 -> showShortToast(getString(ru.wildberries.core.R.string.no_data))
//            is Failure.SimpleFailure -> showLongToast(failure.error)
//            is Failure.HTTP400 -> if (failure.errorMessage.firstIsNotEmpty())
//                showAlertMessageWithoutNegativeButton(
//                    getString(ru.wildberries.core.R.string.ERROR),
//                    failure.errorMessage.getFirstMessageWithDetailLn(),
//                    true
//                )
//            is Failure.HTTP403 -> if (failure.errorMessage.firstIsNotEmpty())
//                showAlertMessageWithoutNegativeButton(
//                    getString(ru.wildberries.core.R.string.ERROR),
//                    getString(ru.wildberries.core.R.string.ERROR_NO_ACCESS),
//                    true
//                )
//            is Failure.HTTP500 -> showAlertMessageWithoutNegativeButton(
//                getString(ru.wildberries.core.R.string.ERROR),
//                getString(ru.wildberries.core.R.string.ERROR_500)
//            )
//            is Failure.SimpleFailureResWithDismissDialog -> showLongToast(getString(failure.error))
//            is Failure.SimpleFailureResWithShowAlertDialog -> showAlertMessageWithoutNegativeButton(
//                getString(ru.wildberries.core.R.string.ERROR),
//                getString(failure.error),
//                true
//            )
//            else -> showLongToast(getString(ru.wildberries.core.R.string.ERROR_UNKNOWN))
//        }
//    }

}