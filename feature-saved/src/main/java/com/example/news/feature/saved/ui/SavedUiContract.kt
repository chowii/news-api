package com.example.news.feature.saved.ui

import com.example.news.feature.saved.SavedUiModel

interface SavedUiContract {
    data class ViewState(
        val data: List<SavedUiModel>,
        val errorState: SavedFullPageErrorCause? = null
    )

    sealed class Actions {
        data class LaunchArticle(val title: String, val url: String): Actions()
    }
}
