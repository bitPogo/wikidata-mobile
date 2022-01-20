/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.page.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.CoroutineScopeDispatcher
import tech.antibytes.util.test.sameAs
import tech.antibytes.wikibase.store.database.page.PageQueries
import tech.antibytes.wikibase.store.mock.client.MwClientStub
import tech.antibytes.wikibase.store.mock.database.PageQueriesStub
import tech.antibytes.wikibase.store.page.domain.DomainContract
import kotlin.test.Test

class PageStoreParameterKoinSpec {
    @Test
    fun `Given resolveEntityStoreParameterModule it holds a given MWClient`() {
        // Given
        val expected = MwClientStub()

        val koin = koinApplication {
            modules(
                resolveEntityStoreParameterModule(
                    expected,
                    PageQueriesStub(),
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
        val expected = PageQueriesStub()

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
        val database: PageQueries = koin.koin.get()

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
                    PageQueriesStub(),
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
                    PageQueriesStub(),
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
