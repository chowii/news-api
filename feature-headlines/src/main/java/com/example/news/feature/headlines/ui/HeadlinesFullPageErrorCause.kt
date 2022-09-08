package com.example.news.feature.headlines.ui

import com.example.news.feature.headlines.R
import com.example.news.base.ui.error.FullPageErrorCause

enum class HeadlinesFullPageErrorCause : FullPageErrorCause {
    NETWORK_ERROR {
        override fun getDrawableRes(): Int = R.drawable.ic_no_network
        override fun getTitle(): Int = R.string.headlines_no_network_title
        override fun getMessage(): Int = R.string.headlines_network_error_message
    },
    SERVER_ERROR {
        override fun getDrawableRes(): Int = R.drawable.ic_server_error
        override fun getTitle(): Int = R.string.headlines_server_error_title
        override fun getMessage(): Int = R.string.headlines_server_error_message
    }, ;

    override fun getPrimaryActionRes(): Int = R.string.headlines_try_again
}