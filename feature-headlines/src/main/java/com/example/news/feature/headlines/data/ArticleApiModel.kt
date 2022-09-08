package com.example.news.feature.headlines.data

data class ArticleApiModel(
    val source: HeadlineSourceApiModel,
    val author: String?,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String?,
    val publishAt: String,
    val content: String
)

fun ArticleApiModel.toUiModel(): ArticleUiModel = ArticleUiModel(
    urlToImage,
    title,
    description,
    author,
    url
)
