package com.example.news.feature.saved.ui

import com.airbnb.epoxy.TypedEpoxyController
import com.example.news.feature.saved.SavedUiModel
import com.example.news.feature.saved.SavedViewModel
import com.example.news.feature.saved.itemSaved

class SavedController(private val viewModel: SavedViewModel) : TypedEpoxyController<List<SavedUiModel>>() {
    override fun buildModels(data: List<SavedUiModel>) {
        data.forEach { uiModel ->
            itemSaved {
                id(uiModel.hashCode())
                data(uiModel)
                clickListener(viewModel)
            }
        }
    }
}
