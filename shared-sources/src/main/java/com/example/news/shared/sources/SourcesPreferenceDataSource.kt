package com.example.news.shared.sources

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SourcesPreferenceDataSource @Inject constructor(
    @ApplicationContext
    val application: Context
) {
    val sourcesSharedPrefs: SharedPreferences
        get() {
            return application.getSharedPreferences(
                "${application.packageName}.sources",
                Context.MODE_PRIVATE
            )
        }
}
