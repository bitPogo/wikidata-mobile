/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.lib.qr.di

import android.graphics.Bitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import org.junit.Test
import org.koin.core.qualifier.named
import tech.antibytes.util.coroutine.result.ResultContract
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract
import tech.antibytes.util.test.isNot
import tech.antibytes.util.test.sameAs
import tech.antibytes.wikidata.lib.qr.domain.DomainContract
import tech.antibytes.wikidata.mock.qr.QrCodeQueriesStub

class QrCodeKoinFactorySpec {
    @Test
    fun `Given initKoin is called with its appropriate Parameter it contains ServiceRepository`() {
        // Given
        val koin = initKoin(
            QrCodeQueriesStub(),
            { CoroutineScope(Dispatchers.Default) },
            { CoroutineScope(Dispatchers.Default) }
        )

        // When
        val repository: DomainContract.ServiceRepository = koin.koin.get()

        // Then
        repository isNot null
    }

    @Test
    fun `Given initKoin is called with its appropriate Parameter it contains StorageRepository`() {
        // Given
        val koin = initKoin(
            QrCodeQueriesStub(),
            { CoroutineScope(Dispatchers.Default) },
            { CoroutineScope(Dispatchers.Default) }
        )

        // When
        val repository: DomainContract.StorageRepository = koin.koin.get()

        // Then
        repository isNot null
    }

    @Test
    fun `Given initKoin is called with its appropriate Parameter it contains MutableSharedFlow`() {
        // Given
        val koin = initKoin(
            QrCodeQueriesStub(),
            { CoroutineScope(Dispatchers.Default) },
            { CoroutineScope(Dispatchers.Default) }
        )

        // When
        val flow: MutableSharedFlow<ResultContract<Bitmap, Exception>> = koin.koin.get()

        // Then
        flow isNot null
    }

    @Test
    fun `Given initKoin is called with its appropriate Parameter it contains SharedFlowWrapper`() {
        // Given
        val koin = initKoin(
            QrCodeQueriesStub(),
            { CoroutineScope(Dispatchers.Default) },
            { CoroutineScope(Dispatchers.Default) }
        )

        // When
        val flow: CoroutineWrapperContract.SharedFlowWrapper<Bitmap, Exception> = koin.koin.get()

        // Then
        flow isNot null
    }

    @Test
    fun `Given initKoin is called with its appropriate Parameter it contains ProducerScopeDispatcher`() {
        // Given
        val expected = CoroutineWrapperContract.CoroutineScopeDispatcher { CoroutineScope(Dispatchers.Default) }

        val koin = initKoin(
            QrCodeQueriesStub(),
            producerScope = expected,
            consumerScope = { CoroutineScope(Dispatchers.Default) }
        )

        // When
        val consumer: CoroutineWrapperContract.CoroutineScopeDispatcher = koin.koin.get(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE))

        // Then
        consumer sameAs expected
    }
}
