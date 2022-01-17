/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.CoroutineScopeDispatcher
import tech.antibytes.util.test.sameAs
import tech.antibytes.wikibase.store.database.entity.EntityQueries
import tech.antibytes.wikibase.store.entity.domain.DomainContract
import tech.antibytes.wikibase.store.mock.EntityQueriesStub
import tech.antibytes.wikibase.store.mock.MwClientStub
import kotlin.test.Test

class EntityStoreParameterKoinSpec {
    @Test
    fun `Given resolveEntityStoreParameterModule it holds a given MWClient`() {
        // Given
        val expected = MwClientStub()

        val koin = koinApplication {
            modules(
                resolveEntityStoreParameterModule(
                    expected,
                    EntityQueriesStub(),
                    { CoroutineScope(Dispatchers.Default) },
                    { CoroutineScope(Dispatchers.Default) }
                )
            )
        }

        // When
        val client: PublicApi.Client = koin.koin.get()

        // Then
        client sameAs expected
    }

    @Test
    fun `Given resolveEntityStoreParameterModule it holds given EntityQueries`() {
        // Given
        val expected = EntityQueriesStub()

        val koin = koinApplication {
            modules(
                resolveEntityStoreParameterModule(
                    MwClientStub(),
                    expected,
                    { CoroutineScope(Dispatchers.Default) },
                    { CoroutineScope(Dispatchers.Default) }
                )
            )
        }

        // When
        val database: EntityQueries = koin.koin.get()

        // Then
        database sameAs expected
    }

    @Test
    fun `Given resolveEntityStoreParameterModule it holds given ProducerDispatcher`() {
        // Given
        val expected = CoroutineScopeDispatcher { CoroutineScope(Dispatchers.Default) }

        val koin = koinApplication {
            modules(
                resolveEntityStoreParameterModule(
                    MwClientStub(),
                    EntityQueriesStub(),
                    expected,
                    { CoroutineScope(Dispatchers.Default) }
                )
            )
        }

        // When
        val dispatcher: CoroutineScopeDispatcher = koin.koin.get(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE))

        // Then
        dispatcher sameAs expected
    }

    @Test
    fun `Given resolveEntityStoreParameterModule it holds given ConsumerDispatcher`() {
        // Given
        val expected = CoroutineScopeDispatcher { CoroutineScope(Dispatchers.Default) }

        val koin = koinApplication {
            modules(
                resolveEntityStoreParameterModule(
                    MwClientStub(),
                    EntityQueriesStub(),
                    { CoroutineScope(Dispatchers.Default) },
                    expected
                )
            )
        }

        // When
        val dispatcher: CoroutineScopeDispatcher = koin.koin.get(named(DomainContract.DomainKoinIds.CONSUMER_SCOPE))

        // Then
        dispatcher sameAs expected
    }
}
