package ru.klyuv.core.common.ui

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import dagger.android.support.DaggerAppCompatActivity
import ru.klyuv.core.R


abstract class BaseActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(getLayoutID())
        setUI(savedInstanceState)
    }

    @LayoutRes
    abstract fun getLayoutID(): Int

    abstract fun setUI(savedInstanceState: Bundle?)

    open fun requestPermissionFun(action1: () -> Unit = {}, action2: () -> Unit = {}) =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (it) action1()
            else action2()
        }

    open fun requestPermissionsFun(action1: () -> Unit = {}, action2: () -> Unit = {}) =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { list ->
            list.forEach {
                if (it.value) action1()
                else action2()
            }
        }

//    fun showFailureError(failure: Failure) {
//        when (failure) {
//            is Failure.NetworkConnection -> showLongToast(getString(R.string.ERROR_CHECK_INTERNET))
//            is Failure.HTTP401 -> if (failure.error.isNotEmpty()) showLongToast(failure.error)
//            is Failure.HTTP401v2 -> if (!failure.errorMessage.firstOrNull()?.message.isNullOrEmpty())
//                showLongToast(getString(R.string.ERROR_FAILED, failure.errorMessage.first().message))
//            is Failure.HTTP422 -> if (!failure.errorMessage.firstOrNull()?.message.isNullOrEmpty())
//                showLongToast(getString(R.string.ERROR_FAILED, failure.errorMessage.first().message))
//            is Failure.TIMEOUT -> showLongToast(getString(R.string.ERROR_TIMEOUT))
//            is Failure.SimpleFailureRes -> showLongToast(getString(failure.error))
//            is Failure.HTTP204 -> showShortToast(getString(R.string.no_data))
//            is Failure.SimpleFailure -> showLongToast(failure.error)
//            is Failure.HTTP400 -> if (!failure.errorMessage.firstOrNull()?.message.isNullOrEmpty())
//                showLongToast(getString(R.string.ERROR_FAILED, failure.errorMessage.first().message))
//            else -> showLongToast(getString(R.string.ERROR_UNKNOWN))
//        }
//    }
}