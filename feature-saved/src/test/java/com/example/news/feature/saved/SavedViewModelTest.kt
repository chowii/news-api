package com.example.news.feature.saved

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.news.feature.saved.ui.SavedFullPageErrorCause
import com.example.news.feature.saved.ui.SavedUiContract
import com.example.news.shared.headlines.HeadlinesPreferencesRepository
import com.example.news.shared.headlines.data.HeadlinePreferenceModel
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

class SavedViewModelTest {

    @Mock
    lateinit var repository: HeadlinesPreferencesRepository

    private lateinit var viewModel: SavedViewModel

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @Captor
    private lateinit var viewStateCaptor: ArgumentCaptor<SavedUiContract.ViewState>
    private val testObserver = lambdaMock<Observer<SavedUiContract.ViewState>>()


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = SavedViewModel(
            repository
        )
    }

    @Test
    fun `Given User Has Saved Articles, When User Launches Saved Screen, Then Emit Saved Articles`() {
        // given
        `when`(repository.getAllArticles()).thenReturn(listOf(HeadlinesPreferenceMock.mockHeadlinesPreference()))
        viewModel.viewState.observeForever(testObserver)

        // when
        viewModel.getSavedArticles()

        // then
        with(viewStateCaptor) {
            verify(testObserver, times(2)).onChanged(capture())
            assertEquals(
                SavedUiContract.ViewState(
                    listOf(
                        SavedUiModel("title 1", "url 1")
                    )
                ),
                value
            )
        }
    }

    @Test
    fun `Given User Has NO Saved Articles, When User Launches Saved Screen, Then Emit Empty Error State`() {
        // given
        `when`(repository.getAllArticles()).thenReturn(emptyList())
        viewModel.viewState.observeForever(testObserver)

        // when
        viewModel.getSavedArticles()

        // then
        with(viewStateCaptor) {
            verify(testObserver, times(2)).onChanged(capture())
            assertEquals(
                SavedUiContract.ViewState(
                    emptyList(),
                    SavedFullPageErrorCause.EMPTY_STATE,
                ),
                value
            )
        }
    }

    @Test
    fun `Given User Has Saved Articles, When User Clicks On Saved Article, Then Emit Launch Article Action`() {
        // given
        `when`(repository.getAllArticles()).thenReturn(listOf(HeadlinesPreferenceMock.mockHeadlinesPreference()))
        val actionsTest = viewModel.actionsObservable.test()

        // when
        viewModel.onSaveArticleClicked("title 1", "url 1")

        // then
        actionsTest.assertValue(SavedUiContract.Actions.LaunchArticle("title 1", "url 1"))
    }

    @Test
    fun `Given User Has Saved Articles, When User Swipes To Remove Saved Article, Then Emit Saved Article`() {
        // given
        `when`(repository.getAllArticles()).thenReturn(HeadlinesPreferenceMock.mockHeadlinesPreferenceList())
        viewModel.viewState.observeForever(testObserver)

        // when
        viewModel.removeSavedArticle("title 2")

        // then
        verify(repository).removeArticle("title 2")
    }
}

inline fun <reified T> lambdaMock(): T = mock(T::class.java)

private object HeadlinesPreferenceMock {
    fun mockHeadlinesPreference(): HeadlinePreferenceModel = HeadlinePreferenceModel(
        "title 1", "url 1"
    )

    fun mockHeadlinesPreferenceList(): List<HeadlinePreferenceModel> = listOf(
        HeadlinePreferenceModel(
            "title 1", "url 1"
        ),
        HeadlinePreferenceModel(
            "title 2", "url 2"
        )
    )
}
