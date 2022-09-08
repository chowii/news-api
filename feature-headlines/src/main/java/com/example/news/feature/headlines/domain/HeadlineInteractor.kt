package com.example.news.feature.headlines.domain

import com.example.news.feature.headlines.data.HeadlineApiModel
import com.example.news.feature.headlines.repository.HeadlineRepository
import com.example.news.shared.sources.SourcesPreferenceRepository
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HeadlineInteractor @Inject constructor(
    private val sourcesPreferenceRepository: SourcesPreferenceRepository,
    private val headlineRepository: HeadlineRepository,
) {

    fun fetchHeadline(): Single<Result<HeadlineApiModel>> {
        val selectedSourceList = sourcesPreferenceRepository.getSelectedSources().orEmpty()
       return headlineRepository.fetchHeadlines(selectedSourceList)
    }
}
