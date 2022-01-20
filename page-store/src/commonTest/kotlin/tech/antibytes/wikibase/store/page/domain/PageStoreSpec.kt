/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.page.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import tech.antibytes.util.coroutine.result.ResultContract
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract
import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fixture.listFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.wikibase.store.mock.SharedFlowWrapperStub
import tech.antibytes.wikibase.store.mock.client.MwClientStub
import tech.antibytes.wikibase.store.mock.database.PageQueriesStub
import tech.antibytes.wikibase.store.mock.transfer.repository.LocalRepositoryStub
import tech.antibytes.wikibase.store.mock.transfer.repository.RemoteRepositoryStub
import tech.antibytes.wikibase.store.page.PageStoreContract
import tech.antibytes.wikibase.store.page.domain.model.EntityId
import tech.antibytes.wikibase.store.page.domain.model.LanguageTag
import tech.antibytes.wikibase.store.page.domain.model.PageModelContract
import tech.antibytes.wikibase.store.page.domain.model.SearchEntry
import tech.antibytes.wikibase.store.page.testScope1
import tech.antibytes.wikibase.store.page.testScope2
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFails

class PageStoreSpec {
    private val fixture = kotlinFixture()
    private val localRepository = LocalRepositoryStub()
    private val remoteRepository = RemoteRepositoryStub()

    @BeforeTest
    fun setUp() {
        localRepository.clear()
        remoteRepository.clear()
    }

    @Test
    fun `It fulfils PageStoreFactory`() {
        PageStore fulfils PageStoreContract.PageStoreFactory::class
    }

    @Test
    fun `Given getInstance is called it retuns a PageStore`() {
        // When
        val store = PageStore.getInstance(
            MwClientStub(),
            PageQueriesStub(),
            { CoroutineScope(Dispatchers.Default) },
            { CoroutineScope(Dispatchers.Default) }
        )

        // Then
        store fulfils PageStoreContract.PageStore::class
    }

    @Test
    fun `Given fetchRandomPage was called it emits a Success containing stored EntityId`() {
        // Given
        val flow = MutableSharedFlow<ResultContract<EntityId, Exception>>()
        val result = Channel<ResultContract<EntityId, Exception>>()

        val id: EntityId = fixture.fixture()

        localRepository.fetchRandomPageId = { id }

        val koin = koinApplication {
            modules(
                module {
                    single<DomainContract.LocalRepository>(named(DomainContract.DomainKoinIds.LOCAL)) {
                        localRepository
                    }

                    single<DomainContract.RemoteRepository>(named(DomainContract.DomainKoinIds.REMOTE)) {
                        remoteRepository
                    }

                    single(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { testScope1 }
                    }

                    single(
                        named(DomainContract.DomainKoinIds.INTERNAL_RANDOM_FLOW)
                    ) {
                        flow
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<EntityId, Exception>>(
                        named(DomainContract.DomainKoinIds.EXTERNAL_RANDOM_FLOW)
                    ) {
                        SharedFlowWrapperStub()
                    }

                    single(
                        named(DomainContract.DomainKoinIds.INTERNAL_SEARCH_FLOW)
                    ) {
                        MutableSharedFlow<ResultContract<List<PageModelContract.SearchEntry>, Exception>>()
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<List<PageModelContract.SearchEntry>, Exception>>(
                        named(DomainContract.DomainKoinIds.EXTERNAL_SEARCH_FLOW)
                    ) {
                        SharedFlowWrapperStub()
                    }
                }
            )
        }

        flow.onEach { item -> result.send(item) }.launchIn(testScope2)

        // When
        PageStore(koin).fetchRandomItem()

        // Then
        runBlockingTest {
            result.receive().unwrap() mustBe id
        }
    }

    @Test
    fun `Given fetchRandomPage was called it emits a Success containing EntityId, while fetching EntityIds and saving them`() {
        // Given
        val flow = MutableSharedFlow<ResultContract<EntityId, Exception>>()
        val result = Channel<ResultContract<EntityId, Exception>>()

        val ids = fixture.listFixture<EntityId>(size = 42)

        localRepository.fetchRandomPageId = { null }

        var capturedIds: List<EntityId> = emptyList()
        localRepository.saveRandomPageIds = { givenIds ->
            capturedIds = givenIds
        }

        remoteRepository.fetchRandomPageIds = { ids }

        val koin = koinApplication {
            modules(
                module {
                    single<DomainContract.LocalRepository>(named(DomainContract.DomainKoinIds.LOCAL)) {
                        localRepository
                    }

                    single<DomainContract.RemoteRepository>(named(DomainContract.DomainKoinIds.REMOTE)) {
                        remoteRepository
                    }

                    single(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { testScope1 }
                    }

                    single(
                        named(DomainContract.DomainKoinIds.INTERNAL_RANDOM_FLOW)
                    ) {
                        flow
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<EntityId, Exception>>(
                        named(DomainContract.DomainKoinIds.EXTERNAL_RANDOM_FLOW)
                    ) {
                        SharedFlowWrapperStub()
                    }

                    single(
                        named(DomainContract.DomainKoinIds.INTERNAL_SEARCH_FLOW)
                    ) {
                        MutableSharedFlow<ResultContract<List<PageModelContract.SearchEntry>, Exception>>()
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<List<PageModelContract.SearchEntry>, Exception>>(
                        named(DomainContract.DomainKoinIds.EXTERNAL_SEARCH_FLOW)
                    ) {
                        SharedFlowWrapperStub()
                    }
                }
            )
        }

        flow.onEach { item -> result.send(item) }.launchIn(testScope2)

        // When
        PageStore(koin).fetchRandomItem()

        // Then
        runBlockingTest {
            result.receive().unwrap() mustBe ids.first()
            capturedIds.size mustBe 41
            capturedIds mustBe ids.subList(1, ids.size)
        }
    }

    @Test
    fun `Given fetchRandomPage was called it emits a Failure with a thrown LocalRepository Error`() {
        // Given
        val flow = MutableSharedFlow<ResultContract<EntityId, Exception>>()
        val result = Channel<ResultContract<EntityId, Exception>>()

        val expected = RuntimeException(fixture.fixture<String>())

        localRepository.fetchRandomPageId = { throw expected }

        val koin = koinApplication {
            modules(
                module {
                    single<DomainContract.LocalRepository>(named(DomainContract.DomainKoinIds.LOCAL)) {
                        localRepository
                    }

                    single<DomainContract.RemoteRepository>(named(DomainContract.DomainKoinIds.REMOTE)) {
                        remoteRepository
                    }

                    single(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { testScope1 }
                    }

                    single(
                        named(DomainContract.DomainKoinIds.INTERNAL_RANDOM_FLOW)
                    ) {
                        flow
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<EntityId, Exception>>(
                        named(DomainContract.DomainKoinIds.EXTERNAL_RANDOM_FLOW)
                    ) {
                        SharedFlowWrapperStub()
                    }

                    single(
                        named(DomainContract.DomainKoinIds.INTERNAL_SEARCH_FLOW)
                    ) {
                        MutableSharedFlow<ResultContract<List<PageModelContract.SearchEntry>, Exception>>()
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<List<PageModelContract.SearchEntry>, Exception>>(
                        named(DomainContract.DomainKoinIds.EXTERNAL_SEARCH_FLOW)
                    ) {
                        SharedFlowWrapperStub()
                    }
                }
            )
        }

        flow.onEach { item -> result.send(item) }.launchIn(testScope2)

        // When
        PageStore(koin).fetchRandomItem()

        // Then
        runBlockingTest {
            val actual = assertFails {
                result.receive().unwrap()
            }

            actual.message mustBe expected.message
        }
    }

    @Test
    fun `Given fetchRandomPage was called it emits a Failure with a thrown RemoteRepository Error`() {
        // Given
        val flow = MutableSharedFlow<ResultContract<EntityId, Exception>>()
        val result = Channel<ResultContract<EntityId, Exception>>()

        val expected = RuntimeException(fixture.fixture<String>())

        localRepository.fetchRandomPageId = { null }

        remoteRepository.fetchRandomPageIds = { throw expected }

        val koin = koinApplication {
            modules(
                module {
                    single<DomainContract.LocalRepository>(named(DomainContract.DomainKoinIds.LOCAL)) {
                        localRepository
                    }

                    single<DomainContract.RemoteRepository>(named(DomainContract.DomainKoinIds.REMOTE)) {
                        remoteRepository
                    }

                    single(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { testScope1 }
                    }

                    single(
                        named(DomainContract.DomainKoinIds.INTERNAL_RANDOM_FLOW)
                    ) {
                        flow
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<EntityId, Exception>>(
                        named(DomainContract.DomainKoinIds.EXTERNAL_RANDOM_FLOW)
                    ) {
                        SharedFlowWrapperStub()
                    }

                    single(
                        named(DomainContract.DomainKoinIds.INTERNAL_SEARCH_FLOW)
                    ) {
                        MutableSharedFlow<ResultContract<List<PageModelContract.SearchEntry>, Exception>>()
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<List<PageModelContract.SearchEntry>, Exception>>(
                        named(DomainContract.DomainKoinIds.EXTERNAL_SEARCH_FLOW)
                    ) {
                        SharedFlowWrapperStub()
                    }
                }
            )
        }

        flow.onEach { item -> result.send(item) }.launchIn(testScope2)

        // When
        PageStore(koin).fetchRandomItem()

        // Then
        runBlockingTest {
            val actual = assertFails {
                result.receive().unwrap()
            }

            actual.message mustBe expected.message
        }
    }

    @Test
    fun `Given searchForItem is called with a SearchTerm and LanguageTag it emits a Success containing SearchEntries`() {
        // Given
        val term: String = fixture.fixture()
        val language: LanguageTag = fixture.fixture()

        val flow = MutableSharedFlow<ResultContract<List<PageModelContract.SearchEntry>, Exception>>()
        val result = Channel<ResultContract<List<PageModelContract.SearchEntry>, Exception>>()

        val expected = listOf(
            SearchEntry(
                id = fixture.fixture(),
                language = fixture.fixture(),
                label = fixture.fixture(),
                description = fixture.fixture()
            ),
            SearchEntry(
                id = fixture.fixture(),
                language = fixture.fixture(),
                label = fixture.fixture(),
                description = fixture.fixture()
            ),
        )

        var capturedTerm: String? = null
        var capturedLanguage: LanguageTag? = null
        remoteRepository.searchForItem = { givenTerm, givenLanguage ->
            capturedTerm = givenTerm
            capturedLanguage = givenLanguage

            expected
        }

        val koin = koinApplication {
            modules(
                module {
                    single<DomainContract.LocalRepository>(named(DomainContract.DomainKoinIds.LOCAL)) {
                        localRepository
                    }

                    single<DomainContract.RemoteRepository>(named(DomainContract.DomainKoinIds.REMOTE)) {
                        remoteRepository
                    }

                    single(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { testScope1 }
                    }

                    single(
                        named(DomainContract.DomainKoinIds.INTERNAL_RANDOM_FLOW)
                    ) {
                        MutableSharedFlow<ResultContract<EntityId, Exception>>()
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<List<PageModelContract.SearchEntry>, Exception>>(
                        named(DomainContract.DomainKoinIds.EXTERNAL_RANDOM_FLOW)
                    ) {
                        SharedFlowWrapperStub()
                    }

                    single(
                        named(DomainContract.DomainKoinIds.INTERNAL_SEARCH_FLOW)
                    ) {
                        flow
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<List<PageModelContract.SearchEntry>, Exception>>(
                        named(DomainContract.DomainKoinIds.EXTERNAL_SEARCH_FLOW)
                    ) {
                        SharedFlowWrapperStub()
                    }
                }
            )
        }

        flow.onEach { item -> result.send(item) }.launchIn(testScope2)

        // When
        PageStore(koin).searchItems(term, language)

        // Then
        runBlockingTest {
            result.receive().unwrap() mustBe expected

            capturedTerm mustBe term
            capturedLanguage mustBe language
        }
    }

    @Test
    fun `Given searchForItem is called with a SearchTerm and LanguageTag it emits a Failure if the RemoteRepository fails`() {
        // Given
        val term: String = fixture.fixture()
        val language: LanguageTag = fixture.fixture()

        val flow = MutableSharedFlow<ResultContract<List<PageModelContract.SearchEntry>, Exception>>()
        val result = Channel<ResultContract<List<PageModelContract.SearchEntry>, Exception>>()

        val expected = RuntimeException(fixture.fixture<String>())

        remoteRepository.searchForItem = { _, _ -> throw expected }

        val koin = koinApplication {
            modules(
                module {
                    single<DomainContract.LocalRepository>(named(DomainContract.DomainKoinIds.LOCAL)) {
                        localRepository
                    }

                    single<DomainContract.RemoteRepository>(named(DomainContract.DomainKoinIds.REMOTE)) {
                        remoteRepository
                    }

                    single(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { testScope1 }
                    }

                    single(
                        named(DomainContract.DomainKoinIds.INTERNAL_RANDOM_FLOW)
                    ) {
                        MutableSharedFlow<ResultContract<EntityId, Exception>>()
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<List<PageModelContract.SearchEntry>, Exception>>(
                        named(DomainContract.DomainKoinIds.EXTERNAL_RANDOM_FLOW)
                    ) {
                        SharedFlowWrapperStub()
                    }

                    single(
                        named(DomainContract.DomainKoinIds.INTERNAL_SEARCH_FLOW)
                    ) {
                        flow
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<List<PageModelContract.SearchEntry>, Exception>>(
                        named(DomainContract.DomainKoinIds.EXTERNAL_SEARCH_FLOW)
                    ) {
                        SharedFlowWrapperStub()
                    }
                }
            )
        }

        flow.onEach { item -> result.send(item) }.launchIn(testScope2)

        // When
        PageStore(koin).searchItems(term, language)

        // Then
        runBlockingTest {
            val actual = assertFails {
                result.receive().unwrap()
            }

            actual.message mustBe expected.message
        }
    }
}
