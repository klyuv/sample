package ru.klyuv.stickybottom.presentation.bottom

import android.app.Dialog
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import ru.klyuv.core.common.extensions.*
import ru.klyuv.stickybottom.R
import ru.klyuv.stickybottom.databinding.FragmentStickyBottomSheetBinding
import ru.klyuv.stickybottom.presentation.StickyBottomViewModel
import ru.klyuv.stickybottom.presentation.StickyState

class StickyBottomSheetDialogFragment : RoundedWithButtonDialogFragment2() {

    private val viewModel by androidLazy { getViewModel<StickyBottomViewModel>(viewModelFactory) }
    private val viewBinding: FragmentStickyBottomSheetBinding by viewBinding(
        FragmentStickyBottomSheetBinding::bind
    )

    private val stickyAdapter by androidLazy {
        ListDelegationAdapter<List<StickyData>>(
            StickyDelegate.init()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sticky_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.accountsRecyclerView.apply {
            adapter = stickyAdapter
            layoutManager = LinearLayoutManager(context)
        }

        val swipeHandler = object: SwipeToDeleteCallback(
            requireContext(),
            R.drawable.ic_vector_delete
        ) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Toast.makeText(requireContext(), "swiped ${viewHolder.absoluteAdapterPosition}", Toast.LENGTH_SHORT).show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(viewBinding.accountsRecyclerView)

        observe(viewModel.data, ::checkState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return configureBottomSheetDialogFragment(
            action = {

            },
            showListener = {

            }
        )
    }

    private fun checkState(state: StickyState) {
        when (state) {
            is StickyState.Progress -> loadingState()
            is StickyState.Success -> successState(state.data)
        }
    }

    private fun loadingState() {
        viewBinding.pbSticky.toVisible()
        viewBinding.accountsRecyclerView.toGone()
    }

    private fun successState(data: List<StickyData>) {
        viewBinding.pbSticky.toGone()
        viewBinding.accountsRecyclerView.toVisible()
        stickyAdapter.items = data
        stickyAdapter.notifyDataSetChanged()
    }
}

/**
 * Изменяет параметры [BottomSheetDialogFragment] для корректного отображения
 * RecyclerView + контент закрепленный снизу.
 *
 * Возвращать в [BottomSheetDialogFragment.onCreateDialog]
 *
 * 1) Устанавливает размеры для конейтера (см. [skipCollapsedAndSetWrapContent])
 * 2) Пропускает состояние [BottomSheetBehavior.STATE_COLLAPSED] при сворачивании диалога свайпом
 *
 * @param isMatchParent -
 */
fun BottomSheetDialogFragment.configureBottomSheetDialogFragment(
    isMatchParent: Boolean = false,
    skipCollapsedAndSetWrapContent: Boolean = true,
    showListener: (() -> Unit)? = null,
    action: (BottomSheetDialog.() -> Unit)? = null
): BottomSheetDialog {
    return BottomSheetDialog(requireContext()).apply {
        setOnShowListener {
            if (skipCollapsedAndSetWrapContent) {
                skipCollapsedAndSetWrapContent(isMatchParent)
            }
            showListener?.invoke()
        }
        behavior.skipCollapsed = true
        action?.invoke(this)
    }
}

/**
 * Изменяет параметры [BottomSheetDialog]
 *
 * Следует вызывать в [BottomSheetDialog.setOnShowListener]
 *
 * 1) При раскрытии меняет состояние диалога на [BottomSheetBehavior.STATE_EXPANDED]
 * с пропуском состояния [BottomSheetBehavior.STATE_COLLAPSED]
 * 2) Задает для контейнера [FrameLayout.LayoutParams.height] = [FrameLayout.LayoutParams.WRAP_CONTENT]
 * необходимо что-бы при неполном наполнении данными (в RecyclerView) диалог был не во весь экран
 */
private fun BottomSheetDialog.skipCollapsedAndSetWrapContent(
    isMatchParent: Boolean = false
) {
    behavior.state = BottomSheetBehavior.STATE_EXPANDED
    val bottomSheet: FrameLayout =
        findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
    val layoutParams: ViewGroup.LayoutParams = bottomSheet.layoutParams
    layoutParams.height =
        if (isMatchParent) FrameLayout.LayoutParams.MATCH_PARENT else FrameLayout.LayoutParams.WRAP_CONTENT
    bottomSheet.layoutParams = layoutParams
}

abstract class SwipeToDeleteCallback(
    context: Context,
    @DrawableRes image: Int,
    @ColorRes color: Int = R.color.colorRed
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

    private val deleteIcon = ContextCompat.getDrawable(context, image)
    private val intrinsicWidth = deleteIcon!!.intrinsicWidth
    private val intrinsicHeight = deleteIcon!!.intrinsicHeight
    private val background = ColorDrawable()
    private val backgroundColor = ContextCompat.getColor(context, color)
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val itemHeight = itemView.height
        val isCanceled = dX == 0f && !isCurrentlyActive

        if (isCanceled) {
            val rect = RectF(
                itemView.right + dX,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat()
            )
            clearCanvas(c, rect)
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        background.color = backgroundColor
        background.setBounds(
            itemView.left + dX.toInt(),
            itemView.top,
            itemView.left,
            itemView.bottom
        )
        background.draw(c)

        val swipeDirs = getSwipeDirs(recyclerView, viewHolder)
        when (swipeDirs) {
            ItemTouchHelper.LEFT -> {
                // Расчитать позицию для иконки
                val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
                val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
                val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
                val deleteIconRight = itemView.right - deleteIconMargin
                val deleteIconBottom = deleteIconTop + intrinsicHeight

                // Отрисовать иконку
                deleteIcon!!.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
                deleteIcon.draw(c)

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
            ItemTouchHelper.RIGHT -> {
                // Расчитать позицию для иконки
                val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
                val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
                val deleteIconLeft = itemView.left - deleteIconMargin - intrinsicWidth
                val deleteIconRight = itemView.left - deleteIconMargin
                val deleteIconBottom = deleteIconTop + intrinsicHeight

                // Отрисовать иконку
                deleteIcon!!.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
                deleteIcon.draw(c)

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
//        // Расчитать позицию для иконки
//        val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
//        val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
//        val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
//        val deleteIconRight = itemView.right - deleteIconMargin
//        val deleteIconBottom = deleteIconTop + intrinsicHeight
//
//        // Отрисовать иконку
//        deleteIcon!!.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
//        deleteIcon.draw(c)
//
//        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(c: Canvas?, rect: RectF) {
        c?.drawRect(rect, clearPaint)
    }
}