package com.example.news.sources.data

data class SourceItemApiModel(
    val id: String,
    val name: String,
    val description: String,
    val url: String,
    val category: String,
    val language: String,
    val country: String,
)

internal fun SourceItemApiModel.toUiModel(isSelected: Boolean) = SourceItemUiModel(
    id,
    category,
    name,
    description,
    isSelected,
)
