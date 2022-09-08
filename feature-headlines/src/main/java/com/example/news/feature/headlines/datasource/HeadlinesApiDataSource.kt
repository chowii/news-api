package com.example.news.feature.headlines.datasource

import com.example.news.feature.headlines.data.HeadlineApiModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface HeadlinesApiDataSource {
    @GET("top-headlines")
    fun getTopHeadlines(@Query(value = "sources", encoded = true) sourceList: String): Single<HeadlineApiModel>
}
