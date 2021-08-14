package ru.klyuv.core.common.extensions

import android.graphics.Paint
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doAfterTextChanged
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.RequestOptions
import ru.klyuv.core.common.ui.SingleClickListener


fun View.toVisible() {
    this.visibility = View.VISIBLE
}

fun View.toGone() {
    this.visibility = View.GONE
}

fun View.toInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.toVisibleOrGone(status: Boolean) {
    this.visibility = if (status) View.VISIBLE else View.GONE
}

fun View.toVisibleOrInvisible(status: Boolean) {
    this.visibility = if (status) View.VISIBLE else View.INVISIBLE
}

fun ImageView.loadImage(@DrawableRes resId: Int) =
    Glide.with(context).load(resId)
        .apply(RequestOptions().override(this.width, this.height))
        .into(this)

fun ImageView.loadImgWithCache(url: String?, @DrawableRes errorDrawable: Int) {
    if (!url.isNullOrEmpty()) Glide.with(this)
        .load(GlideUrl(url))
        .apply(
            RequestOptions()
                .centerCrop()
                .error(errorDrawable)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
        )
        .into(this)
    else setImageDrawable(ResourcesCompat.getDrawable(resources, errorDrawable, null))
}

//fun ImageView.setByteArrayWithoutCache(data: ByteArray, @DrawableRes errorDrawable: Int) {
//    if (data.isNotEmpty()) Glide.with(this)
//        .load(data)
//        .apply(
//            RequestOptions()
//                .error(errorDrawable)
//                .priority(Priority.HIGH)
//        )
//        .into(this)
//    else {
//        setImageDrawable(resources.getDrawable(errorDrawable, null))
//    }
//}

fun EditText.textIsEmpty(): Boolean =
    this.text.toString().isEmpty()

fun EditText.textIsNotEmpty(): Boolean =
    this.text.toString().isNotEmpty()

inline fun View.afterMeasured(crossinline block: () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                block()
            }
        }
    })
}

inline fun TextView.doAfterTextChangedAfterMeasured(crossinline action: (text: Editable?) -> Unit) {
    this.afterMeasured { doAfterTextChanged(action) }
}

fun View.getStringById(@StringRes resId: Int) =
    this.context.getString(resId)

fun View.getStringById(@StringRes resId: Int, vararg formatArgs: Any?) =
    this.context.getString(resId, *formatArgs)

fun View.setOnSingleClickListener(intervalMs: Long = 1000, click: (View) -> Unit) {
    val singleClickListener = SingleClickListener(intervalMs) {
        click(it)
    }
    setOnClickListener(singleClickListener)
}

fun TextView.setUnderlined(underlined: Boolean) {
    paintFlags =
        if (underlined) paintFlags or Paint.UNDERLINE_TEXT_FLAG
        else paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
}

fun TextView.setTextColorByResource(@ColorRes color: Int) =
    context?.let { setTextColor(it.getColor(color)) }

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun View.animateSliding(
    isUnfolded: Boolean,
    slideDuration: Long = 200,
    onAnimationEnd: () -> Unit = {}
) {
    val unfoldedY = -measuredHeight.toFloat()
    this.animate()
        .translationY(if (isUnfolded) unfoldedY else 0F)
        .setDuration(slideDuration)
        .withEndAction(onAnimationEnd)
        .start()
}
