package ru.klyuv.stickybottom.presentation.bottom

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.klyuv.core.common.ui.DaggerBottomDialogFragment
import ru.klyuv.stickybottom.R
import javax.inject.Inject


abstract class RoundedWithButtonDialogFragment : DaggerBottomDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    abstract val button: Button

    private var buttonLayoutParams: ConstraintLayout.LayoutParams? = null
    private var collapsedMargin = 0
    private var buttonHeight = 0
    private var expandedHeight = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.setOnShowListener { dialogInterface: DialogInterface? -> setupRatio((dialogInterface as BottomSheetDialog?)!!) }

        (dialog as BottomSheetDialog).behavior.addBottomSheetCallback(object :
            BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset > 0) //Sliding happens from 0 (Collapsed) to 1 (Expanded) - if so, calculate margins
                    buttonLayoutParams!!.topMargin =
                        ((expandedHeight - buttonHeight - collapsedMargin) * slideOffset + collapsedMargin).toInt() else  //If not sliding above expanded, set initial margin
                    buttonLayoutParams!!.topMargin =
                        collapsedMargin
                button.layoutParams =
                    buttonLayoutParams //Set layout params to button (margin from top)
            }
        })

        return dialog
    }

    private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<FrameLayout?>(R.id.design_bottom_sheet) ?: return

        //Retrieve button parameters
        buttonLayoutParams = ConstraintLayout.LayoutParams(
            button.layoutParams as ConstraintLayout.LayoutParams
        )

        //Retrieve bottom sheet parameters
        BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
        val bottomSheetLayoutParams = bottomSheet.layoutParams
        bottomSheetLayoutParams.height = getBottomSheetDialogDefaultHeight()

        expandedHeight = bottomSheetLayoutParams.height
        val peekHeight: Int =
            (expandedHeight).toInt() //Peek height to 70% of expanded height (Change based on your view)

        //Setup bottom sheet
        bottomSheet.layoutParams = bottomSheetLayoutParams
        BottomSheetBehavior.from(bottomSheet).skipCollapsed = false
        BottomSheetBehavior.from(bottomSheet).peekHeight = peekHeight
        BottomSheetBehavior.from(bottomSheet).isHideable = true

        //Calculate button margin from top
        buttonHeight =
            button.height + 40 //How tall is the button + experimental distance from bottom (Change based on your view)

        collapsedMargin = peekHeight - buttonHeight //Button margin in bottom sheet collapsed state

        buttonLayoutParams!!.topMargin = collapsedMargin
        button.layoutParams = buttonLayoutParams

        settingsMargin(buttonHeight)
    }

    abstract fun settingsMargin(buttonHeight: Int)

    //Calculates height for 90% of fullscreen
    private fun getBottomSheetDialogDefaultHeight(): Int {
        return getWindowHeight()
    }

    //Calculates window height for fullscreen use
    private fun getWindowHeight(): Int {
        val displayMetrics = requireContext().resources.displayMetrics
        return displayMetrics.heightPixels
    }


}

abstract class RoundedWithButtonDialogFragment2 : DaggerBottomDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return resolve()
    }

}

fun BottomSheetDialogFragment.resolve(): BottomSheetDialog {
    return BottomSheetDialog(requireContext()).apply {
        setOnShowListener {
            resolve()
        }
        behavior.skipCollapsed = true
    }
}

fun BottomSheetDialog.resolve() {
    behavior.state = BottomSheetBehavior.STATE_EXPANDED
    val bottomSheet: FrameLayout =
        findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
    val layoutParams: ViewGroup.LayoutParams = bottomSheet.layoutParams
    layoutParams.height = FrameLayout.LayoutParams.WRAP_CONTENT
    bottomSheet.layoutParams = layoutParams
}
