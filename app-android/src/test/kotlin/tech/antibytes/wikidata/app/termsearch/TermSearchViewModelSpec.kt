/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termsearch

import androidx.lifecycle.ViewModel
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Before
import org.junit.Test
import tech.antibytes.util.coroutine.result.Failure
import tech.antibytes.util.coroutine.result.ResultContract
import tech.antibytes.util.coroutine.result.Success
import tech.antibytes.util.coroutine.wrapper.SharedFlowWrapper
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract
import tech.antibytes.wikibase.store.entity.lang.EntityStoreError
import tech.antibytes.wikibase.store.page.domain.model.PageModelContract
import tech.antibytes.wikidata.app.util.UtilContract
import tech.antibytes.wikidata.mock.EntityStoreStub
import tech.antibytes.wikidata.mock.MwLocaleStub
import tech.antibytes.wikidata.mock.PageStoreStub
import tech.antibytes.wikidata.mock.SearchEntry
import java.lang.RuntimeException

class TermSearchViewModelSpec {
    private val fixture = kotlinFixture()
    private val pageFlow: MutableSharedFlow<ResultContract<List<PageModelContract.SearchEntry>, Exception>> = MutableSharedFlow()
    private val pageFlowSurface = SharedFlowWrapper.getInstance(
        pageFlow
    ) { CoroutineScope(Dispatchers.Default) }

    private val pageStore = PageStoreStub(
        SharedFlowWrapper.getInstance(MutableSharedFlow()) { CoroutineScope(Dispatchers.Default) },
        pageFlowSurface,
    )

    private val currentLanguage = MutableStateFlow(mockk<UtilContract.MwLocale>())

    private val entityFlow: MutableStateFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>> = MutableStateFlow(
        Failure(EntityStoreError.InitialState())
    )

    private val entitySurfaceFlow = SharedFlowWrapper.getInstance(
        entityFlow
    ) { CoroutineScope(Dispatchers.Default) }

    private val entityStore = EntityStoreStub(entitySurfaceFlow)

    @Before
    fun setUp() {
        entityFlow.update { Failure(EntityStoreError.InitialState()) }
        currentLanguage.value = mockk()

        pageStore.clear()
        entityStore.clear()
    }

    @Test
    fun `It fulfils TermSearchViewModel`() {
        val viewModel = TermSearchViewModel(
            pageStore, entityStore,
            currentLanguage
        )
        viewModel fulfils TermSearchContract.TermSearchViewModel::class
        viewModel fulfils ViewModel::class
    }

    @Test
    fun `Its default query state is a empty String`() {
        TermSearchViewModel(
            pageStore, entityStore,
            currentLanguage
        ).query.value mustBe ""
    }

    @Test
    fun `Its default result state is a empty List`() {
        TermSearchViewModel(
            pageStore, entityStore,
            currentLanguage
        ).result.value mustBe emptyList()
    }

    @Test
    fun `Given a query is set it emits the new query state`() {
        // Given
        val query: String = fixture.fixture()
        val result = Channel<String>()

        // When
        val viewModel = TermSearchViewModel(
            pageStore, entityStore,
            currentLanguage
        )
        CoroutineScope(Dispatchers.Default).launch {
            viewModel.query.collectLatest { state -> result.send(state) }
        }

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() mustBe ""
            }
        }

        // When
        viewModel.setQuery(query)

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() mustBe query
            }
        }
    }

    @Test
    fun `Given search is called it delegates the query state to the store`() {
        // Given
        val query: String = fixture.fixture()
        val language: UtilContract.MwLocale = MwLocaleStub(fixture.fixture(), fixture.fixture(), fixture.fixture())
        val searchResult = Channel<List<PageModelContract.SearchEntry>>()

        currentLanguage.value = language

        val expected = listOf(
            SearchEntry(
                id = fixture.fixture(),
                language = language.toLanguageTag(),
                label = fixture.fixture<String>(),
                description = null
            )
        )

        var capturedQuery: String? = null
        var capturedLanguage: String? = null
        pageStore.searchItems = { givenQuery, givenLanguage ->
            capturedQuery = givenQuery
            capturedLanguage = givenLanguage

            CoroutineScope(Dispatchers.Default).launch {
                pageFlow.emit(Success(expected))
            }
        }

        // When
        val viewModel = TermSearchViewModel(
            pageStore, entityStore,
            currentLanguage
        )
        CoroutineScope(Dispatchers.Default).launch {
            viewModel.result.collectLatest { state -> searchResult.send(state) }
        }

        // Then
        runBlocking {
            withTimeout(2000) {
                searchResult.receive() mustBe emptyList()
            }
        }

        // When
        viewModel.setQuery(query)
        viewModel.search()

        // Then
        runBlocking {
            withTimeout(2000) {
                searchResult.receive() mustBe expected
            }
        }

        capturedLanguage mustBe language.toLanguageTag().replace('_', '-').lowercase()
        capturedQuery mustBe query
    }

    @Test
    fun `Given search is called it delegates the query state to the store, while ignoring Errors`() {
        // Given
        val query: String = fixture.fixture()
        val language: UtilContract.MwLocale = MwLocaleStub(fixture.fixture(), fixture.fixture(), fixture.fixture())
        val searchResult = Channel<List<PageModelContract.SearchEntry>>()

        currentLanguage.value = language

        var capturedQuery: String? = null
        var capturedLanguage: String? = null
        pageStore.searchItems = { givenQuery, givenLanguage ->
            capturedQuery = givenQuery
            capturedLanguage = givenLanguage

            CoroutineScope(Dispatchers.Default).launch {
                pageFlow.emit(Failure(RuntimeException()))
            }
        }

        // When
        val viewModel = TermSearchViewModel(
            pageStore,
            entityStore,
            currentLanguage
        )
        CoroutineScope(Dispatchers.Default).launch {
            viewModel.result.collectLatest { state -> searchResult.send(state) }
        }

        // Then
        runBlocking {
            withTimeout(2000) {
                searchResult.receive() mustBe emptyList()
            }
        }

        // When
        viewModel.setQuery(query)
        viewModel.search()

        // Then
        runBlocking {
            delay(10)
        }

        capturedLanguage mustBe language.toLanguageTag().replace('_', '-').lowercase()
        capturedQuery mustBe query
        viewModel.result.value mustBe emptyList()
    }

    @Test
    fun `Given select is called with a Index, it fetches the Entity at given Position`() {
        // Given
        val index = 1
        val id: String = fixture.fixture()
        val language: UtilContract.MwLocale = MwLocaleStub(fixture.fixture(), fixture.fixture(), fixture.fixture())

        currentLanguage.value = language

        var capturedId: String? = null
        var capturedLanguage: String? = null
        entityStore.fetchEntity = { givenId, givenLanguage ->
            capturedId = givenId
            capturedLanguage = givenLanguage
        }

        // When
        val viewModel = TermSearchViewModel(
            pageStore,
            entityStore,
            currentLanguage
        )

        runBlocking {
            pageFlow.emit(
                Success(
                    listOf(
                        SearchEntry(
                            id = fixture.fixture(),
                            language = fixture.fixture(),
                            label = fixture.fixture<String>(),
                            description = null
                        ),
                        SearchEntry(
                            id = id,
                            language = fixture.fixture(),
                            label = fixture.fixture<String>(),
                            description = null
                        ),
                        SearchEntry(
                            id = fixture.fixture(),
                            language = fixture.fixture(),
                            label = fixture.fixture<String>(),
                            description = null
                        )
                    )
                )
            )
        }

        viewModel.select(index)

        // Then
        capturedId mustBe id
        capturedLanguage mustBe language.toLanguageTag().replace('_', '-').lowercase()
    }
}
