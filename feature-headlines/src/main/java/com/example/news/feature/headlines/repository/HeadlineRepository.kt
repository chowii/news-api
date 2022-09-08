package com.example.news.feature.headlines.repository

import com.example.news.base.network.concurrency.SchedulersProvider
import com.example.news.base.network.util.toResult
import com.example.news.feature.headlines.data.HeadlineApiModel
import com.example.news.feature.headlines.datasource.HeadlinesApiDataSource
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HeadlineRepository @Inject constructor(
    private val headlinesApiDataSource: HeadlinesApiDataSource,
    private val schedulersProvider: SchedulersProvider,
) {
    fun fetchHeadlines(selectedSourceList: Set<String>): Single<Result<HeadlineApiModel>> {
        val selectedSources = selectedSourceList.joinToString(separator = ",") { it }
        return headlinesApiDataSource.getTopHeadlines(selectedSources)
            .subscribeOn(schedulersProvider.io())
            .toResult()
    }
}
