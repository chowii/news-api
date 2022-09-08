package com.example.news.shared.headlines

import com.example.news.shared.headlines.data.HeadlinePreferenceModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HeadlinesPreferencesRepository @Inject constructor(
    private val headlinesPrefsDataSource: HeadlinesPreferencesDataSource
) {

    fun getAllArticles(): List<HeadlinePreferenceModel> {
        return headlinesPrefsDataSource.articlesSharedPrefs.all.map { (k,v) ->
            HeadlinePreferenceModel(k, v as String)
        }
    }

    fun saveArticle(articleTitle: String, articleUrl: String) {
        headlinesPrefsDataSource.articlesSharedPrefs.edit()
            .putString(articleTitle, articleUrl)
            .apply()
    }

    fun removeArticle(articleTitle: String) {
        headlinesPrefsDataSource.articlesSharedPrefs.edit()
            .remove(articleTitle)
            .apply()
    }

    fun hasArticle(articleTitle: String): Boolean =
        headlinesPrefsDataSource.articlesSharedPrefs.all.containsKey(articleTitle)
}
