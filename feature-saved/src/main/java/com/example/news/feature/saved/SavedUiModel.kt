package com.example.news.feature.saved

import com.example.news.shared.headlines.data.HeadlinePreferenceModel


data class SavedUiModel(
    val title: String,
    val url: String,
)

fun HeadlinePreferenceModel.toUiModel(): SavedUiModel = SavedUiModel(title, url)
