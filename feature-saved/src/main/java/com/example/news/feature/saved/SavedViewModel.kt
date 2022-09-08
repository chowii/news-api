package com.example.news.feature.saved

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.news.feature.saved.ui.SavedFullPageErrorCause
import com.example.news.feature.saved.ui.SavedUiContract
import com.example.news.shared.headlines.HeadlinesPreferencesRepository
import com.example.news.shared.headlines.data.HeadlinePreferenceModel
import com.example.news.base.ui.error.FullPageErrorStateClickHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val headlinePrefsRepository: HeadlinesPreferencesRepository,
): ViewModel(), OnSavedArticleClickListener, FullPageErrorStateClickHandler {

    private val mutableViewState = MutableLiveData<SavedUiContract.ViewState>()
    val viewState: LiveData<SavedUiContract.ViewState> = mutableViewState

    private val actionsSubject = PublishSubject.create<SavedUiContract.Actions>()
    val actionsObservable: Observable<SavedUiContract.Actions> = actionsSubject

    fun getSavedArticles() {
        mutableViewState.value = SavedUiContract.ViewState(emptyList(), null)
        val articleList = headlinePrefsRepository.getAllArticles()
            .map(HeadlinePreferenceModel::toUiModel)

        mutableViewState.value = SavedUiContract.ViewState(
            articleList.ifEmpty { emptyList() },
            if (articleList.isEmpty()) SavedFullPageErrorCause.EMPTY_STATE else null
        )
    }

    override fun onSaveArticleClicked(title: String, url: String) {
        actionsSubject.onNext(SavedUiContract.Actions.LaunchArticle(title, url))
    }

    override fun onPrimaryActionClicked() {
        getSavedArticles()
    }

    fun removeSavedArticle(title: String) {
        headlinePrefsRepository.removeArticle(title)
        getSavedArticles()
    }
}

interface OnSavedArticleClickListener {
    fun onSaveArticleClicked(title: String, url: String)
}
