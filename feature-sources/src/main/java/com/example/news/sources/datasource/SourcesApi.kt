package com.example.news.sources.datasource

import com.example.news.sources.data.SourcesApiModel
import io.reactivex.Single
import retrofit2.http.GET

interface SourcesApi {
    @GET("top-headlines/sources?language=en")
    fun getSources(): Single<SourcesApiModel>
}
