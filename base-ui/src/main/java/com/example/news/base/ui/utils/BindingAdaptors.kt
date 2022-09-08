package com.example.news.base.ui.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.bumptech.glide.Glide

@BindingAdapter(value = ["vectorSrc"], requireAll = false)
fun setVectorDrawableResCompat(
    imageView: ImageView,
    @DrawableRes drawableRes: Int,
) {
    val context = imageView.context
    if (drawableRes == UiConstants.INVALID_RESOURCE_ID) {
        imageView.setImageDrawable(null)
    } else {
        val drawable = VectorDrawableCompat.create(context.resources, drawableRes, context.theme)
        imageView.setImageDrawable(drawable)
    }
}


@BindingAdapter("visibleIfAvailable")
fun setVisibilityIfAvailable(textView: TextView, @StringRes stringRes: Int) {
    if (stringRes == UiConstants.INVALID_RESOURCE_ID) {
        textView.visibility = View.GONE
    } else {
        textView.text = textView.context.getText(stringRes)
        textView.visibility = View.VISIBLE
    }
}

@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, url: String?) {
    Glide.with(imageView)
        .load(url)
        .into(imageView)
}
