package com.example.news.sources.domain

import com.example.news.base.network.util.value
import com.example.news.shared.sources.SourcesPreferenceRepository
import com.example.news.sources.data.SourceItemApiModel
import com.example.news.sources.data.SourceItemUiModel
import com.example.news.sources.data.SourcesApiModel
import com.example.news.sources.data.SourcesUiModel
import com.example.news.sources.repository.SourcesRepository
import io.reactivex.Single
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class SourcesInteractorTest {

    @Mock
    lateinit var sourcesRepository: SourcesRepository

    @Mock
    lateinit var sourcesPreferenceRepository: SourcesPreferenceRepository

    private lateinit var interactor: SourcesInteractor

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        interactor = SourcesInteractor(
            sourcesRepository,
            sourcesPreferenceRepository,
        )
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `Given User Has Selected Sources, When Sources Api Is Success, Then Emit Source As Selected Source`() {
        // given
        `when`(sourcesRepository.fetchSources()).thenReturn(Single.just(Result.success(SourcesApiMock.createSourcesApiModelMock())))
        `when`(sourcesPreferenceRepository.getSelectedSources()).thenReturn(mutableSetOf("id 2"))

        // when
        val testObserver = interactor.fetchSources().test()

        // when
        assertEquals(1, testObserver.valueCount())
        assertEquals(
            SourceItemUiModel(
                "id 2",
                "category 2",
                "name 2",
                "desc 2",
                true,
            ),
            testObserver.values().first().value?.sourceList?.find { it.isSelected }
        )
        assertEquals(
            Result.success(
                SourcesUiModel(
                    listOf(
                        SourceItemUiModel(
                            "id 1",
                            "category 1",
                            "name 1",
                            "desc 1",
                            false,
                        ),
                        SourceItemUiModel(
                            "id 2",
                            "category 2",
                            "name 2",
                            "desc 2",
                            true,
                        ),
                    )
                )
            ),
            testObserver.values().first()
        )
    }

    @Test
    fun `Given User Has NO Selected Sources, When Sources Api Is Success, Then Mutate Source to Selected State`() {
        // given
        `when`(sourcesRepository.fetchSources()).thenReturn(Single.just(Result.success(SourcesApiMock.createSourcesApiModelMock())))
        `when`(sourcesPreferenceRepository.getSelectedSources()).thenReturn(mutableSetOf())

        // when
        val testObserver = interactor.fetchSources().test()

        // when
        assertEquals(1, testObserver.valueCount())
        assertEquals(
            Result.success(
                SourcesUiModel(
                    listOf(
                        SourceItemUiModel(
                            "id 1",
                            "category 1",
                            "name 1",
                            "desc 1",
                            false,
                        ),
                        SourceItemUiModel(
                            "id 2",
                            "category 2",
                            "name 2",
                            "desc 2",
                            false,
                        ),
                    )
                )
            ),
            testObserver.values().first()
        )
    }
}

object SourcesApiMock {
    fun createSourcesApiModelMock() = SourcesApiModel(
        "ok",
        listOf(
            SourceItemApiModel(
                "id 1",
                "name 1",
                "desc 1",
                "url 1",
                "category 1",
                "lang 1",
                "country 1",
            ),
            SourceItemApiModel(
                "id 2",
                "name 2",
                "desc 2",
                "url 2",
                "category 2",
                "lang 2",
                "country 2",
            ),
        ),
    )
}
