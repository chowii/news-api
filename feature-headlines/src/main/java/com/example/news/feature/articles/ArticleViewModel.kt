package com.example.news.feature.articles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.news.feature.articles.ui.ArticleUiContract
import com.example.news.shared.headlines.HeadlinesPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val headlinesPrefsRepository: HeadlinesPreferencesRepository
) : ViewModel(), OnSaveCheckChangeListener {

    private val mutableViewState = MutableLiveData<ArticleUiContract.ViewState>()
    val viewState: LiveData<ArticleUiContract.ViewState> = mutableViewState

    fun fetchArticleSaved(articleTitle: String, url: String) {
        val hasArticle = headlinesPrefsRepository.hasArticle(articleTitle)
        mutableViewState.value = ArticleUiContract.ViewState(articleTitle, url, hasArticle)
    }

    override fun onSaveCheckChanged(title: String, url: String, isChecked: Boolean) {
        if (isChecked) {
            headlinesPrefsRepository.saveArticle(title, url)
        } else {
            headlinesPrefsRepository.removeArticle(title)
        }
    }
}

interface OnSaveCheckChangeListener {
    fun onSaveCheckChanged(title: String, url: String, isChecked: Boolean)
}
