package ru.klyuv.core.common.extensions

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.klyuv.core.R


enum class PermissionsError {
    CAMERA
}

fun Activity.showErrorPermissionMessage(errorCode: PermissionsError) {
    when (errorCode) {
        PermissionsError.CAMERA -> showErrorPermissionMessage(
            this.getString(R.string.attention),
            this.getString(R.string.camera_error_permission_message)
        )
    }
}

fun Activity.showErrorPermissionMessage(title: String, message: String): AlertDialog =
    MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
        .setTitle(title)
        .setMessage(message)
        .setCancelable(false)
        .setNegativeButton(this.getString(R.string.cancel)) { dialog, _ -> dialog.cancel() }
        .setPositiveButton(this.getString(R.string.settings)) { dialog, _ -> goToSettings() }
        .show()

fun Activity.goToSettings() {
    val appSettingsIntent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.parse("package:$packageName")
    )
    startActivityForResult(appSettingsIntent, 1)
}