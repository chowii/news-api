package com.example.news.feature.headlines.ui

import com.airbnb.epoxy.TypedEpoxyController
import com.example.news.feature.headlines.data.HeadlineUiModel
import com.example.news.feature.headlines.itemHeadline
import com.example.news.feature.headlines.presentation.HeadlinesViewModel

class HeadlineController(
    private val viewModel: HeadlinesViewModel
) : TypedEpoxyController<HeadlineUiModel>() {
    override fun buildModels(data: HeadlineUiModel) {
        data.articleList.forEach {
            itemHeadline {
                id(it.hashCode())
                data(it)
                clickListener(viewModel)
            }
        }
    }
}
