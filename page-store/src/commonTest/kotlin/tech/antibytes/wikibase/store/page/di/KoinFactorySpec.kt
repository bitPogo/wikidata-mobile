/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.page.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import org.koin.core.qualifier.named
import tech.antibytes.util.coroutine.result.ResultContract
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.CoroutineScopeDispatcher
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.SharedFlowWrapper
import tech.antibytes.util.test.isNot
import tech.antibytes.util.test.sameAs
import tech.antibytes.wikibase.store.mock.client.MwClientStub
import tech.antibytes.wikibase.store.mock.database.PageQueriesStub
import tech.antibytes.wikibase.store.page.domain.DomainContract
import tech.antibytes.wikibase.store.page.domain.model.EntityId
import tech.antibytes.wikibase.store.page.domain.model.PageModelContract
import kotlin.test.Test

class KoinFactorySpec {
    @Test
    fun `Given initKoin is called with its appropriate Parameter it contains RemoteRepository`() {
        // Given
        val koin = initKoin(
            MwClientStub(),
            PageQueriesStub(),
            { CoroutineScope(Dispatchers.Default) },
            { CoroutineScope(Dispatchers.Default) }
        )

        // When
        val repository: DomainContract.RemoteRepository = koin.koin.get(named(DomainContract.DomainKoinIds.REMOTE))

        // Then
        repository isNot null
    }

    @Test
    fun `Given initKoin is called with its appropriate Parameter it contains LocalRepository`() {
        // Given
        val koin = initKoin(
            MwClientStub(),
            PageQueriesStub(),
            { CoroutineScope(Dispatchers.Default) },
            { CoroutineScope(Dispatchers.Default) }
        )

        // When
        val repository: DomainContract.LocalRepository = koin.koin.get(named(DomainContract.DomainKoinIds.LOCAL))

        // Then
        repository isNot null
    }

    @Test
    fun `Given initKoin is called with its appropriate Parameter it contains MutableSharedFlow for RandomIds`() {
        // Given
        val koin = initKoin(
            MwClientStub(),
            PageQueriesStub(),
            { CoroutineScope(Dispatchers.Default) },
            { CoroutineScope(Dispatchers.Default) }
        )

        // When
        val flow: MutableSharedFlow<ResultContract<EntityId, Exception>> = koin.koin.get(
            named(DomainContract.DomainKoinIds.INTERNAL_RANDOM_FLOW)
        )

        // Then
        flow isNot null
    }

    @Test
    fun `Given initKoin is called with its appropriate Parameter it contains MutableSharedFlow for SearchIds`() {
        // Given
        val koin = initKoin(
            MwClientStub(),
            PageQueriesStub(),
            { CoroutineScope(Dispatchers.Default) },
            { CoroutineScope(Dispatchers.Default) }
        )

        // When
        val flow: MutableSharedFlow<ResultContract<List<PageModelContract.SearchEntry>, Exception>> = koin.koin.get(
            named(DomainContract.DomainKoinIds.INTERNAL_SEARCH_FLOW)
        )

        // Then
        flow isNot null
    }

    @Test
    fun `Given initKoin is called with its appropriate Parameter it contains SharedFlowWrapper for RandomIds`() {
        // Given
        val koin = initKoin(
            MwClientStub(),
            PageQueriesStub(),
            { CoroutineScope(Dispatchers.Default) },
            { CoroutineScope(Dispatchers.Default) }
        )

        // When
        val flow: SharedFlowWrapper<EntityId, Exception> = koin.koin.get(
            named(DomainContract.DomainKoinIds.EXTERNAL_RANDOM_FLOW)
        )

        // Then
        flow isNot null
    }

    @Test
    fun `Given initKoin is called with its appropriate Parameter it contains SharedFlowWrapper for ItemSearch`() {
        // Given
        val koin = initKoin(
            MwClientStub(),
            PageQueriesStub(),
            { CoroutineScope(Dispatchers.Default) },
            { CoroutineScope(Dispatchers.Default) }
        )

        // When
        val flow: SharedFlowWrapper<List<PageModelContract.SearchEntry>, Exception> = koin.koin.get(
            named(DomainContract.DomainKoinIds.EXTERNAL_SEARCH_FLOW)
        )

        // Then
        flow isNot null
    }

    @Test
    fun `Given initKoin is called with its appropriate Parameter it contains ProducerScopeDispatcher`() {
        // Given
        val expected = CoroutineScopeDispatcher { CoroutineScope(Dispatchers.Default) }

        val koin = initKoin(
            MwClientStub(),
            PageQueriesStub(),
            producerScope = expected,
            consumerScope = { CoroutineScope(Dispatchers.Default) }
        )

        // When
        val consumer: CoroutineScopeDispatcher = koin.koin.get(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE))

        // Then
        consumer sameAs expected
    }
}
