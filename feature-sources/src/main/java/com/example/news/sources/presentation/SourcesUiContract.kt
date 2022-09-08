package com.example.news.sources.presentation

import com.example.news.sources.data.SourcesUiModel
import com.example.news.sources.ui.SourcesFullPageErrorCause

interface SourcesUiContract {
    data class ViewState(
        val isLoading: Boolean,
        val data: SourcesUiModel?,
        val errorState: SourcesFullPageErrorCause?,
    )
}