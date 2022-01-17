/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.integration

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.CoroutineScopeDispatcher
import tech.antibytes.util.test.isNot
import tech.antibytes.util.test.sameAs
import tech.antibytes.wikibase.store.entity.di.initKoin
import tech.antibytes.wikibase.store.entity.domain.DomainContract
import tech.antibytes.wikibase.store.mock.EntityQueriesStub
import tech.antibytes.wikibase.store.mock.MwClientStub
import kotlin.test.Test

class KoinFactorySpec {
    @Test
    fun `Given initKoin is called with its appropriate Parameter it contains RemoteRepository`() {
        // Given
        val koin = initKoin(
            MwClientStub(),
            EntityQueriesStub(),
            { CoroutineScope(Dispatchers.Default) },
            { CoroutineScope(Dispatchers.Default) }
        )

        // When
        val repository: DomainContract.Repository = koin.koin.get(named(DomainContract.DomainKoinIds.REMOTE))

        // Then
        repository isNot null
    }

    @Test
    fun `Given initKoin is called with its appropriate Parameter it contains LocalRepository`() {
        // Given
        val koin = initKoin(
            MwClientStub(),
            EntityQueriesStub(),
            { CoroutineScope(Dispatchers.Default) },
            { CoroutineScope(Dispatchers.Default) }
        )

        // When
        val repository: DomainContract.Repository = koin.koin.get(named(DomainContract.DomainKoinIds.LOCAL))

        // Then
        repository isNot null
    }

    @Test
    fun `Given initKoin is called with its appropriate Parameter it contains ProducerScopeDispatcher`() {
        // Given
        val expected = CoroutineScopeDispatcher { CoroutineScope(Dispatchers.Default) }

        val koin = initKoin(
            MwClientStub(),
            EntityQueriesStub(),
            producerScope = expected,
            { CoroutineScope(Dispatchers.Default) }
        )

        // When
        val producer: CoroutineScopeDispatcher = koin.koin.get(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE))

        // Then
        producer sameAs expected
    }

    @Test
    fun `Given initKoin is called with its appropriate Parameter it contains ConsumerScopeDispatcher`() {
        // Given
        val expected = CoroutineScopeDispatcher { CoroutineScope(Dispatchers.Default) }

        val koin = initKoin(
            MwClientStub(),
            EntityQueriesStub(),
            producerScope = { CoroutineScope(Dispatchers.Default) },
            consumerScope = expected
        )

        // When
        val consumer: CoroutineScopeDispatcher = koin.koin.get(named(DomainContract.DomainKoinIds.CONSUMER_SCOPE))

        // Then
        consumer sameAs expected
    }
}
