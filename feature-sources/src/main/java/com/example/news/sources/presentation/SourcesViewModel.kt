package com.example.news.sources.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.news.base.network.concurrency.SchedulersProvider
import com.example.news.base.network.connection.ConnectionManager
import com.example.news.base.network.util.value
import com.example.news.sources.data.SourceItemUiModel
import com.example.news.sources.data.SourcesUiModel
import com.example.news.sources.domain.SourcesInteractor
import com.example.news.sources.ui.SourcesFullPageErrorCause
import com.example.news.base.ui.error.FullPageErrorStateClickHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

@HiltViewModel
class SourcesViewModel @Inject constructor(
    private val sourcesInteractor: SourcesInteractor,
    private val schedulers: SchedulersProvider,
    private val connectionManager: ConnectionManager,
) : ViewModel(), FullPageErrorStateClickHandler, SourceClickListener {
    private val disposable = CompositeDisposable()

    private val mutableViewState = MutableLiveData<SourcesUiContract.ViewState>()
    val viewState: LiveData<SourcesUiContract.ViewState> = mutableViewState

    fun fetchSourceList() {
        mutableViewState.value = SourcesUiContract.ViewState(true, null, null)
        disposable += sourcesInteractor.fetchSources()
            .observeOn(schedulers.mainUiThread())
            .subscribe { result ->
                if (result.isSuccess) {
                    val data = result.value?.sourceList
                        ?.let(this::makeCategorySentenceCase)
                    mutableViewState.value = SourcesUiContract.ViewState(
                        false,
                        data,
                        null
                    )
                } else {
                    handleFetchSourcesError(result)
                }
            }
    }

    private fun makeCategorySentenceCase(sourceList: List<SourceItemUiModel>): SourcesUiModel {
        return sourceList.map { item ->
            item.copy(category = item.category.replaceFirstChar { it.titlecase() })
        }.let(::SourcesUiModel)
    }

    private fun handleFetchSourcesError(result: Result<SourcesUiModel>) {
        val errorState =
            if (connectionManager.isNoConnectivityException(result.exceptionOrNull())) {
                SourcesFullPageErrorCause.NETWORK_ERROR
            } else {
                SourcesFullPageErrorCause.SERVER_ERROR
            }
        mutableViewState.value = SourcesUiContract.ViewState(
            false,
            null,
            errorState
        )
    }

    override fun onSourceClicked(source: SourceItemUiModel) {
        sourcesInteractor.updateSelectedSource(source.id, !source.isSelected)
        val data = mutableViewState.value?.data
        val sourceList = data?.sourceList
        val updatedSourceItem = sourceList?.map {
            if (it.id == source.id) it.copy(isSelected = !source.isSelected) else it
        }
            ?.let(data::copy)
        mutableViewState.value = SourcesUiContract.ViewState(false, updatedSourceItem, null)
    }

    override fun onPrimaryActionClicked() {
        fetchSourceList()
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}

interface SourceClickListener {
    fun onSourceClicked(sourceItemUiModel: SourceItemUiModel)
}
