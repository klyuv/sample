package ru.klyuv.core.common.ui

import android.view.View
import java.util.concurrent.atomic.AtomicBoolean

class SingleClickListener(
    private val intervalMs: Long,
    private val onSafeCLick: (View) -> Unit
) : View.OnClickListener {

    private var canClick = AtomicBoolean(true)

    override fun onClick(v: View?) {
        if (canClick.getAndSet(false)) {
            v?.run {
                postDelayed({
                    canClick.set(true)
                }, intervalMs)
                onSafeCLick(v)
            }
        }
    }
}
