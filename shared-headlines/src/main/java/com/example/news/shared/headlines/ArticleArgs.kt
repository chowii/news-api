package com.example.news.shared.headlines

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArticleArgs(
    val url: String,
    val title: String
) : Parcelable {
    companion object {
        const val ARTICLE_ARGS_KEY = "ARTICLE_ARGS_KEY"
    }
}
