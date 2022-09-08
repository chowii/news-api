package com.example.news.feature.headlines.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.news.base.network.concurrency.SchedulersProvider
import com.example.news.base.network.connection.ConnectionManager
import com.example.news.base.network.util.value
import com.example.news.feature.headlines.data.HeadlineApiModel
import com.example.news.feature.headlines.data.toUiModel
import com.example.news.feature.headlines.domain.HeadlineInteractor
import com.example.news.feature.headlines.ui.HeadlinesFullPageErrorCause
import com.example.news.base.ui.error.FullPageErrorStateClickHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class HeadlinesViewModel @Inject constructor(
    private val headlineApi: HeadlineInteractor,
    private val schedulersProvider: SchedulersProvider,
    private val connectionManager: ConnectionManager,
) : ViewModel(), FullPageErrorStateClickHandler, OnHeadlineItemClickListener {

    private val disposable = CompositeDisposable()
    private val mutableViewState = MutableLiveData<HeadlineUiContract.ViewState>()
    val viewState: LiveData<HeadlineUiContract.ViewState> = mutableViewState

    private val uiActionsSubject = PublishSubject.create<HeadlineUiContract.Actions>()
    val uiActionsObservable: Observable<HeadlineUiContract.Actions> = uiActionsSubject

    fun fetchHeadlines() {
        mutableViewState.value = HeadlineUiContract.ViewState(true, null, null)
        disposable += headlineApi.fetchHeadline()
            .observeOn(schedulersProvider.mainUiThread())
            .subscribeOn(schedulersProvider.io())
            .subscribe { result ->
                val data = result.value?.toUiModel()
                if (result.isSuccess) {
                    mutableViewState.value = HeadlineUiContract.ViewState(
                        false,
                        data,
                        null
                    )
                } else {
                    handleErrorFetchError(result)
                }
            }
    }

    private fun handleErrorFetchError(result: Result<HeadlineApiModel>) {
        val errorCause = when {
            connectionManager.isNoConnectivityException(result.exceptionOrNull()) ->
                HeadlinesFullPageErrorCause.NETWORK_ERROR
            else ->
                HeadlinesFullPageErrorCause.SERVER_ERROR
        }
        mutableViewState.value = HeadlineUiContract.ViewState(
            false,
            null,
            errorCause,
        )
    }

    override fun onHeadlineClicked(url: String, title: String) {
        uiActionsSubject.onNext(HeadlineUiContract.Actions.LaunchArticle(url, title))
    }

    override fun onPrimaryActionClicked() {
        fetchHeadlines()
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}

interface OnHeadlineItemClickListener {
    fun onHeadlineClicked(url: String, title: String)
}
