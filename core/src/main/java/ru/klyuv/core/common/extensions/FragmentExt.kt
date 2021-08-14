package ru.klyuv.core.common.extensions

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.klyuv.core.R

fun Fragment.showErrorPermissionMessage(errorCode: PermissionsError) =
    activity?.showErrorPermissionMessage(errorCode)

fun Fragment.showAlertMessageWithoutNegativeButton(
    title: String,
    message: String,
    cancelable: Boolean = false,
    action: () -> Unit = {}
): AlertDialog =
    MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
        .setTitle(title)
        .setMessage(message)
        .setCancelable(cancelable)
        .setPositiveButton(this.getString(R.string.ok)) { _, _ -> action() }
        .show()

fun Fragment.showAlertMessageWithNegativeListener(
    title: String,
    message: String,
    cancelable: Boolean = false,
    actionPositive: () -> Unit = {},
    actionNegative: () -> Unit = {}
): AlertDialog =
    MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
        .setTitle(title)
        .setMessage(message)
        .setCancelable(cancelable)
        .setNegativeButton(this.getString(R.string.no)) { dialog, _ ->
            dialog.cancel()
        }
        .setPositiveButton(this.getString(R.string.yes)) { _, _ -> actionPositive() }
        .setOnCancelListener { dialog ->
            actionNegative()
            dialog.cancel()
        }
        .show()

fun Fragment.showLongToast(message: String) {
    activity?.showLongToast(message)
}

fun Fragment.showShortToast(message: String) {
    activity?.showShortToast(message)
}