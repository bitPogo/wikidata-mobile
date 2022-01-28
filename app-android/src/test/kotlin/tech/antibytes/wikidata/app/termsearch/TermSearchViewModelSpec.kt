/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termsearch

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
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
import tech.antibytes.wikibase.store.page.domain.model.PageModelContract
import tech.antibytes.wikidata.mock.PageStoreStub
import tech.antibytes.wikidata.mock.SearchEntry
import java.lang.RuntimeException
import java.util.Locale

class TermSearchViewModelSpec {
    private val fixture = kotlinFixture()
    private val flow: MutableSharedFlow<ResultContract<List<PageModelContract.SearchEntry>, Exception>> = MutableSharedFlow()
    private val flowSurface = SharedFlowWrapper.getInstance(
        flow
    ) { CoroutineScope(Dispatchers.Default) }

    private val store = PageStoreStub(
        SharedFlowWrapper.getInstance(MutableSharedFlow()) { CoroutineScope(Dispatchers.Default) },
        flowSurface,
    )

    @Before
    fun setUp() {
        store.clear()
    }

    @Test
    fun `It fulfils TermSearchViewModel`() {
        val viewModel = TermSearchViewModel(store)
        viewModel fulfils TermSearchContract.TermSearchViewModel::class
        viewModel fulfils ViewModel::class
    }

    @Test
    fun `Its default query state is a empty String`() {
        TermSearchViewModel(store).query.value mustBe ""
    }

    @Test
    fun `Its default result state is a empty List`() {
        TermSearchViewModel(store).result.value mustBe emptyList()
    }

    @Test
    fun `Given a query is set it emits the new query state`() {
        // Given
        val query: String = fixture.fixture()
        val result = Channel<String>()

        // When
        val viewModel = TermSearchViewModel(store)
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
        val language = Locale.KOREA
        val searchResult = Channel<List<PageModelContract.SearchEntry>>()

        val expected = listOf(
            SearchEntry(
                id = fixture.fixture(),
                language = language.toLanguageTag().replace('_', '-'),
                label = fixture.fixture<String>(),
                description = null
            )
        )

        var capturedQuery: String? = null
        var capturedLanguage: String? = null
        store.searchItems = { givenQuery, givenLanguage ->
            capturedQuery = givenQuery
            capturedLanguage = givenLanguage

            CoroutineScope(Dispatchers.Default).launch {
                flow.emit(Success(expected))
            }
        }

        // When
        val viewModel = TermSearchViewModel(store)
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
        viewModel.search(language)

        // Then
        runBlocking {
            withTimeout(2000) {
                searchResult.receive() mustBe expected
            }
        }

        capturedLanguage mustBe language.toLanguageTag().replace('_', '-')
        capturedQuery mustBe query
    }

    @Test
    fun `Given search is called it delegates the query state to the store, while ignoring Errors`() {
        // Given
        val query: String = fixture.fixture()
        val language = Locale.KOREA
        val searchResult = Channel<List<PageModelContract.SearchEntry>>()

        var capturedQuery: String? = null
        var capturedLanguage: String? = null
        store.searchItems = { givenQuery, givenLanguage ->
            capturedQuery = givenQuery
            capturedLanguage = givenLanguage

            CoroutineScope(Dispatchers.Default).launch {
                flow.emit(Failure(RuntimeException()))
            }
        }

        // When
        val viewModel = TermSearchViewModel(store)
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
        viewModel.search(language)

        // Then
        runBlocking {
            delay(10)
        }

        capturedLanguage mustBe language.toLanguageTag().replace('_', '-')
        capturedQuery mustBe query
        viewModel.result.value mustBe emptyList()
    }
}
