package com.example.news.feature.saved.ui

import com.example.news.base.ui.error.FullPageErrorCause
import com.example.news.feature.saved.R

enum class SavedFullPageErrorCause : FullPageErrorCause {
    EMPTY_STATE {
        override fun getDrawableRes(): Int = R.drawable.ic_empty_state
        override fun getTitle(): Int = R.string.saved_empty_title
        override fun getMessage(): Int = R.string.saved_empty_message
    };
    override fun getPrimaryActionRes(): Int = R.string.saved_try_again
}
