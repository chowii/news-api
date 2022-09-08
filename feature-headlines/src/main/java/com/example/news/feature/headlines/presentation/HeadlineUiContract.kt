package com.example.news.feature.headlines.presentation

import com.example.news.feature.headlines.data.HeadlineUiModel
import com.example.news.feature.headlines.ui.HeadlinesFullPageErrorCause


interface HeadlineUiContract {
    data class ViewState(
        val isLoading: Boolean,
        val data: HeadlineUiModel?,
        val errorState: HeadlinesFullPageErrorCause?
    )

    sealed class Actions {
        data class LaunchArticle(val url: String, val title: String) : Actions()
    }
}