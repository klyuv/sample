package ru.klyuv.core.common.extensions

import androidx.fragment.app.Fragment

fun Fragment.showErrorPermissionMessage(errorCode: PermissionsError) =
    activity?.showErrorPermissionMessage(errorCode)