package com.example.news.shared.sources

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SourcesPreferenceRepository @Inject constructor(
    private val sourcesPreference: SourcesPreferenceDataSource
) {

    fun storeSelectedSource(sourceId: String, isSelected: Boolean) {
        val selectedSources = getSelectedSources()?.toMutableSet()
        if (isSelected) {
            selectedSources?.add(sourceId)
        } else {
            selectedSources?.remove(sourceId)
        }
        sourcesPreference.sourcesSharedPrefs.edit()
            .putStringSet(SOURCES_SET_KEY, selectedSources)
            .apply()
    }

    fun getSelectedSources(): MutableSet<String>? = sourcesPreference
        .sourcesSharedPrefs.getStringSet(SOURCES_SET_KEY, setOf())

    companion object {
        private const val SOURCES_SET_KEY = "SELECTED_SOURCES_SET_KEY"
    }
}
