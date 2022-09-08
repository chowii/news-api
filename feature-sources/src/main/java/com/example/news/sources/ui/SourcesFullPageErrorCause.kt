package com.example.news.sources.ui

import com.example.news.sources.R
import com.example.news.base.ui.error.FullPageErrorCause

enum class SourcesFullPageErrorCause : FullPageErrorCause {
    NETWORK_ERROR {
        override fun getDrawableRes(): Int = R.drawable.ic_no_network
        override fun getTitle(): Int = R.string.no_network_title
        override fun getMessage(): Int = R.string.network_error_message
    },
    SERVER_ERROR {
        override fun getDrawableRes(): Int = R.drawable.ic_server_error
        override fun getTitle(): Int = R.string.server_error_title
        override fun getMessage(): Int = R.string.server_error_message
    };

    override fun getPrimaryActionRes(): Int = R.string.try_again
}
