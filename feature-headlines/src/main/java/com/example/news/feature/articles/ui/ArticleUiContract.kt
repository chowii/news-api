package com.example.news.feature.articles.ui

interface ArticleUiContract {
    data class ViewState(
        val title: String,
        val url: String,
        val isArticleSaved: Boolean
    )
}
