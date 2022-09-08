package com.example.news.sources.repository

import com.example.news.base.network.concurrency.SchedulersProvider
import com.example.news.base.network.util.toResult
import com.example.news.sources.datasource.SourcesApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SourcesRepository @Inject constructor(
    private val sourcesApi: SourcesApi,
    private val schedulers: SchedulersProvider,
) {

    internal fun fetchSources() = sourcesApi.getSources()
        .subscribeOn(schedulers.io())
        .toResult()
}
