package com.example.news.sources.data

import com.google.gson.annotations.SerializedName

data class SourcesApiModel(
    val status: String,
    @SerializedName("sources")
    val sourceList: List<SourceItemApiModel>
)
