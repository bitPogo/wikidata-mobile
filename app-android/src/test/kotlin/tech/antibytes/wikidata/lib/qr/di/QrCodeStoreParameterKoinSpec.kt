/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.lib.qr.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.junit.Test
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.CoroutineScopeDispatcher
import tech.antibytes.util.test.sameAs
import tech.antibytes.wikibase.store.database.QrCodeQueries
import tech.antibytes.wikidata.lib.qr.domain.DomainContract
import tech.antibytes.wikidata.mock.qr.QrCodeQueriesStub

class QrCodeStoreParameterKoinSpec {
    @Test
    fun `Given resolveQrCodeStoreParameterModule it holds given QrCodeQueries`() {
        // Given
        val expected = QrCodeQueriesStub()

        val koin = koinApplication {
            modules(
                resolveQrCodeStoreParameterModule(
                    expected,
                    { CoroutineScope(Dispatchers.Default) },
                    { CoroutineScope(Dispatchers.Default) }
                )
            )
        }

        // When
        val database: QrCodeQueries = koin.koin.get()

        // Then
        database sameAs expected
    }

    @Test
    fun `Given resolveQrCodeStoreParameterModule it holds given ProducerDispatcher`() {
        // Given
        val expected = CoroutineScopeDispatcher { CoroutineScope(Dispatchers.Default) }

        val koin = koinApplication {
            modules(
                resolveQrCodeStoreParameterModule(
                    QrCodeQueriesStub(),
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
    fun `Given resolveQrCodeStoreParameterModule it holds given ConsumerDispatcher`() {
        // Given
        val expected = CoroutineScopeDispatcher { CoroutineScope(Dispatchers.Default) }

        val koin = koinApplication {
            modules(
                resolveQrCodeStoreParameterModule(
                    QrCodeQueriesStub(),
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
