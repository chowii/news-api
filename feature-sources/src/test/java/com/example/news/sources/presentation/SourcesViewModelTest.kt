package com.example.news.sources.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.news.base.network.concurrency.TestSchedulersProvider
import com.example.news.base.network.connection.ConnectionManager
import com.example.news.base.network.connection.NoConnectivityException
import com.example.news.sources.data.SourceItemUiModel
import com.example.news.sources.data.SourcesUiModel
import com.example.news.sources.domain.SourcesInteractor
import com.example.news.sources.ui.SourcesFullPageErrorCause
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.*
import org.mockito.Mockito.*

internal class SourcesViewModelTest {

    @Mock
    lateinit var sourcesInteractor: SourcesInteractor

    @Mock
    lateinit var connectionManager: ConnectionManager

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @Captor
    private lateinit var viewStateCaptor: ArgumentCaptor<SourcesUiContract.ViewState>
    private val testObserver = lambdaMock<Observer<SourcesUiContract.ViewState>>()
    private val schedulers = TestSchedulersProvider()

    private lateinit var sourcesViewModel: SourcesViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        sourcesViewModel = SourcesViewModel(
            sourcesInteractor,
            schedulers,
            connectionManager
        )
    }

    @Test
    fun `Given User Launches Sources Screen, When Api Responds, Then Show Data`() {
        // given
        `when`(sourcesInteractor.fetchSources()).thenReturn(Single.just(Result.success(SourcesMock.createSourcesMock())))
        sourcesViewModel.viewState.observeForever(testObserver)

        // when
        sourcesViewModel.fetchSourceList()

        // then
        with(viewStateCaptor) {
            verify(testObserver, times(2)).onChanged(capture())
            assertEquals(
                SourcesUiContract.ViewState(
                    false,
                    SourcesUiModel(
                        listOf(
                            SourceItemUiModel(
                                "id",
                                "Source category",
                                "source name",
                                "source desc",
                                false
                            )
                        ),
                    ),
                    null
                ),
                value
            )
        }
    }

    @Test
    fun `Given User Has No Internet Connectivity, When User Launches Sources Screen, Then Show Network Error`() {
        // given
        `when`(sourcesInteractor.fetchSources()).thenReturn(Single.just(Result.failure(NoConnectivityException())))
        `when`(connectionManager.isNoConnectivityException(any())).thenReturn(true)
        sourcesViewModel.viewState.observeForever(testObserver)

        // when
        sourcesViewModel.fetchSourceList()

        // then
        with(viewStateCaptor) {
            verify(testObserver, times(2)).onChanged(capture())
            assertEquals(
                SourcesUiContract.ViewState(
                    false,
                    null,
                    SourcesFullPageErrorCause.NETWORK_ERROR
                ),
                value
            )
        }
    }

    @Test
    fun `Given User Has Internet Connectivity And User Launches Sources Screen, When Api Has error, Then Show Server Error`() {
        // given
        `when`(sourcesInteractor.fetchSources()).thenReturn(Single.just(Result.failure(NoConnectivityException())))
        `when`(connectionManager.isNoConnectivityException(any())).thenReturn(false)
        sourcesViewModel.viewState.observeForever(testObserver)

        // when
        sourcesViewModel.fetchSourceList()

        // then
        with(viewStateCaptor) {
            verify(testObserver, times(2)).onChanged(capture())
            assertEquals(
                SourcesUiContract.ViewState(
                    false,
                    null,
                    SourcesFullPageErrorCause.SERVER_ERROR
                ),
                value
            )
        }
    }
}

inline fun <reified T> lambdaMock(): T = mock(T::class.java)

internal object SourcesMock {
    internal fun createSourcesMock(): SourcesUiModel {
        return SourcesUiModel(
            listOf(
                SourceItemUiModel(
                    "id",
                    "source category",
                    "source name",
                    "source desc",
                    false
                )
            ),
        )
    }
}
