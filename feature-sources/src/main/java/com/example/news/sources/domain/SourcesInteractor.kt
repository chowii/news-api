package com.example.news.sources.domain

import com.example.news.base.network.util.value
import com.example.news.shared.sources.SourcesPreferenceRepository
import com.example.news.sources.data.SourcesUiModel
import com.example.news.sources.data.toUiModel
import com.example.news.sources.repository.SourcesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SourcesInteractor @Inject constructor(
    private val sourcesRepository: SourcesRepository,
    private val sourcesPreferenceRepository: SourcesPreferenceRepository,
) {

    fun fetchSources() = sourcesRepository.fetchSources()
        .map { result ->
            if (result.isSuccess && result.value != null) {
                val prefsSourceIdList = sourcesPreferenceRepository.getSelectedSources().orEmpty()
                val fetchedSourceList = result.value?.sourceList

                val selectedSourcesUiModel = fetchedSourceList
                    ?.map { id -> id.toUiModel(prefsSourceIdList.contains(id.id)) }
                    .orEmpty()
                Result.success(SourcesUiModel(selectedSourcesUiModel))
            } else {
                Result.failure(result.exceptionOrNull() ?: Throwable("Unknown Exception at Fetching Sources"))
            }
        }

    fun updateSelectedSource(sourceId: String, isSelected: Boolean) {
        sourcesPreferenceRepository.storeSelectedSource(sourceId, isSelected)
    }
}
