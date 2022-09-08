package com.example.news.shared.headlines

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HeadlinesPreferencesDataSource @Inject constructor(
    @ApplicationContext
    private val context: Context,
) {
    val articlesSharedPrefs: SharedPreferences
        get() {
            return context.getSharedPreferences(
                "${context.packageName}.articles",
                Context.MODE_PRIVATE,
            )
        }
}
