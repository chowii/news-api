package com.example.news.feature.headlines.data

import com.google.gson.annotations.SerializedName

data class HeadlineApiModel(
    val status: String,
    @SerializedName("articles")
    val articleList: List<ArticleApiModel>
)

internal fun HeadlineApiModel.toUiModel(): HeadlineUiModel = HeadlineUiModel(
    articleList.map(ArticleApiModel::toUiModel)
)
