package com.example.news.feature.headlines.presentation

import com.example.news.base.network.concurrency.TestSchedulersProvider
import com.example.news.base.network.connection.ConnectionManager
import com.example.news.base.network.connection.NoConnectivityException
import com.example.news.feature.headlines.data.*
import com.example.news.feature.headlines.domain.HeadlineInteractor
import com.example.news.feature.headlines.presentation.HeadlineUiContract
import com.example.news.feature.headlines.presentation.HeadlinesViewModel
import com.example.news.feature.headlines.ui.HeadlinesFullPageErrorCause
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class HeadlinesViewModelTest {

    @Mock
    private lateinit var interactor: HeadlineInteractor

    @Mock
    lateinit var connectionManager: ConnectionManager

    private lateinit var viewModel: HeadlinesViewModel

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @Captor
    private lateinit var viewStateCaptor: ArgumentCaptor<HeadlineUiContract.ViewState>
    private val testObserver = lambdaMock<Observer<HeadlineUiContract.ViewState>>()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = HeadlinesViewModel(
            interactor,
            TestSchedulersProvider(),
            connectionManager,
        )
    }

    @Test
    fun `When User Launches Headlines Screen, Then Emit Headlines`() {
        // given
        `when`(interactor.fetchHeadline()).thenReturn(Single.just(Result.success(HeadlinesMock.createHeadlinesMock())))
        viewModel.viewState.observeForever(testObserver)

        // when
        viewModel.fetchHeadlines()

        // then
        with(viewStateCaptor) {
            verify(testObserver, times(2)).onChanged(capture())
            assert(viewStateCaptor.allValues.first().isLoading)
            assertEquals(
                HeadlineUiContract.ViewState(
                    false,
                    HeadlineUiModel(
                        listOf(
                            ArticleUiModel(
                                "img url",
                                "title",
                                "desc",
                                "author",
                                "url"
                            )
                        )
                    ),
                    null
                ),
                value
            )
        }
    }

    @Test
    fun `Given User Has No Internet Connectivity, When User Launches Headlines Screen, Then Emit Network Error`() {
        // given
        `when`(interactor.fetchHeadline()).thenReturn(Single.just(Result.failure(NoConnectivityException())))
        `when`(connectionManager.isNoConnectivityException(any())).thenReturn(true)
        viewModel.viewState.observeForever(testObserver)

        // when
        viewModel.fetchHeadlines()

        // then
        with(viewStateCaptor) {
            verify(testObserver, times(2)).onChanged(capture())
            assertEquals(
                HeadlineUiContract.ViewState(
                    false,
                    null,
                    HeadlinesFullPageErrorCause.NETWORK_ERROR
                ),
                value
            )
        }
    }

    @Test
    fun `Given User Has Internet Connectivity And User Launches Headlines Screen, When Api Has error, Then Show Server Error`() {
        // given
        `when`(interactor.fetchHeadline()).thenReturn(Single.just(Result.failure(NoConnectivityException()
        )))
        `when`(connectionManager.isNoConnectivityException(any())).thenReturn(false)
        viewModel.viewState.observeForever(testObserver)

        // when
        viewModel.fetchHeadlines()

        // then
        with(viewStateCaptor) {
            verify(testObserver, times(2)).onChanged(capture())
            assertEquals(
                HeadlineUiContract.ViewState(
                    false,
                    null,
                    HeadlinesFullPageErrorCause.SERVER_ERROR
                ),
                value
            )
        }
    }
}

inline fun <reified T> lambdaMock(): T = mock(T::class.java)

private object HeadlinesMock {
    fun createHeadlinesMock(): HeadlineApiModel {
        return HeadlineApiModel(
            "status",
            listOf(
                ArticleApiModel(
                    HeadlineSourceApiModel(
                        "source id",
                        "source name",
                    ),
                    "author",
                    "title",
                    "desc",
                    "url",
                    "img url",
                    "publish at",
                    "content"
                )
            )
        )
    }
}
