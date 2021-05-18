package ru.klyuv.barcode.common

import android.view.SurfaceHolder

inline fun SurfaceHolder.addSurfaceCreatedListener(crossinline action: (holder: SurfaceHolder) -> Unit) {
    this.addHolderListener(onSurfaceCreated = action)
}

inline fun SurfaceHolder.addSurfaceChangedListener(crossinline action: (holder: SurfaceHolder, format: Int, width: Int, height: Int) -> Unit) {
    this.addHolderListener(onSurfaceChanged = action)
}

inline fun SurfaceHolder.addSurfaceDestroyedListener(crossinline action: (holder: SurfaceHolder) -> Unit) {
    this.addHolderListener(onSurfaceDestroyed = action)
}

inline fun SurfaceHolder.addHolderListener(
    crossinline onSurfaceCreated: (holder: SurfaceHolder) -> Unit = { _ -> },
    crossinline onSurfaceChanged: (holder: SurfaceHolder, format: Int, width: Int, height: Int) -> Unit = { _, _, _, _ -> },
    crossinline onSurfaceDestroyed: (holder: SurfaceHolder) -> Unit = { _ -> }
): SurfaceHolder.Callback {
    val listener = object : SurfaceHolder.Callback {

        override fun surfaceCreated(p0: SurfaceHolder) {
            onSurfaceCreated(p0)
        }

        override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
            onSurfaceChanged(p0, p1, p2, p3)
        }

        override fun surfaceDestroyed(p0: SurfaceHolder) {
            onSurfaceDestroyed(p0)
        }
    }
    this.addCallback(listener)
    return listener
}