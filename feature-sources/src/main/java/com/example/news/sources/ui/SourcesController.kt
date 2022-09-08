package com.example.news.sources.ui

import com.airbnb.epoxy.TypedEpoxyController
import com.example.news.sources.data.SourcesUiModel
import com.example.news.sources.itemSourcesDetails
import com.example.news.sources.presentation.SourcesViewModel

class SourcesController(private val viewModel: SourcesViewModel) :
    TypedEpoxyController<SourcesUiModel>() {
    override fun buildModels(data: SourcesUiModel) {
        data.sourceList.forEach {
            itemSourcesDetails {
                id(it.id)
                data(it)
                clickListener(viewModel)
            }
        }
    }
}
