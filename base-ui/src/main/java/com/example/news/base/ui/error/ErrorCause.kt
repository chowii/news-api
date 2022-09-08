package com.example.news.base.ui.error

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.news.base.ui.utils.UiConstants

interface FullPageErrorCause {
    @DrawableRes
    fun getDrawableRes(): Int

    @StringRes
    fun getTitle(): Int

    @StringRes
    fun getMessage(): Int

    @StringRes
    fun getPrimaryActionRes(): Int {
        return UiConstants.INVALID_RESOURCE_ID
    }
}
