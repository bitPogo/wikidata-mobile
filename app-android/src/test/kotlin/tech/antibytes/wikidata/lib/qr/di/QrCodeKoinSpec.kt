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
import kotlinx.coroutines.flow.SharedFlow
import org.junit.Test
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import tech.antibytes.util.coroutine.result.ResultContract
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract
import tech.antibytes.util.test.isNot
import tech.antibytes.util.test.sameAs
import tech.antibytes.wikidata.lib.qr.domain.DomainContract
import tech.antibytes.wikidata.mock.SharedFlowWrapperFactoryStub
import tech.antibytes.wikidata.mock.SharedFlowWrapperStub

class QrCodeKoinSpec {
    @Test
    fun `Given resolveQrCodeStoreModule is called, it contains MutableSharedFlow`() {
        // Given
        val koin = koinApplication {
            modules(
                resolveQrCodeStoreModule()
            )
        }

        // When
        val flow: MutableSharedFlow<ResultContract<Bitmap, Exception>> = koin.koin.get()

        // Then
        flow isNot null
    }

    @Test
    fun `Given resolveQrCodeStoreModule is called, it contains MutableSharedFlow, while not creating it twice`() {
        // Given
        val koin = koinApplication {
            modules(
                resolveQrCodeStoreModule()
            )
        }

        // When
        val flow1: MutableSharedFlow<ResultContract<Bitmap, Exception>> = koin.koin.get()
        val flow2: MutableSharedFlow<ResultContract<Bitmap, Exception>> = koin.koin.get()

        // Then
        flow1 sameAs flow2
    }

    @Test
    fun `Given resolveQrCodeStoreModule is called, it contains SharedFlowWrapperFactory`() {
        // Given
        val koin = koinApplication {
            modules(
                resolveQrCodeStoreModule()
            )
        }

        // When
        val factory: CoroutineWrapperContract.SharedFlowWrapperFactory = koin.koin.get()

        // Then
        factory isNot null
    }

    @Test
    fun `Given resolveQrCodeStoreModule is called, it creates a SharedFlowWrapper`() {
        // Given
        val koin = koinApplication {
            modules(
                resolveQrCodeStoreModule(),
                module {
                    single<CoroutineWrapperContract.CoroutineScopeDispatcher>(named(DomainContract.DomainKoinIds.CONSUMER_SCOPE)) {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { CoroutineScope(Dispatchers.Default) }
                    }
                }
            )
        }

        // When
        val flow: CoroutineWrapperContract.SharedFlowWrapper<Bitmap, Exception> = koin.koin.get()

        // Then
        flow isNot null
    }

    @Test
    fun `Given resolveQrCodeStoreModule is called, it contains SharedFlowWrapper`() {
        // Given
        val dispatcher = CoroutineWrapperContract.CoroutineScopeDispatcher { CoroutineScope(Dispatchers.Default) }
        val internalFlow = MutableSharedFlow<ResultContract<Bitmap, Exception>>()

        var capturedFlow: SharedFlow<ResultContract<Bitmap, Exception>>? = null
        var capturedDispatcher: CoroutineWrapperContract.CoroutineScopeDispatcher? = null
        val expected = SharedFlowWrapperStub<Bitmap, Exception>()

        val factory = SharedFlowWrapperFactoryStub<Bitmap, Exception> { givenFlow, givenDispatcher ->
            capturedFlow = givenFlow
            capturedDispatcher = givenDispatcher

            expected
        }

        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolveQrCodeStoreModule(),
                module {
                    single<CoroutineWrapperContract.SharedFlowWrapperFactory> {
                        factory
                    }

                    single(named(DomainContract.DomainKoinIds.CONSUMER_SCOPE)) { dispatcher }

                    single {
                        internalFlow
                    }
                }
            )
        }

        // When
        val flow: CoroutineWrapperContract.SharedFlowWrapper<Bitmap, Exception> = koin.koin.get()

        // Then
        flow sameAs expected
        capturedDispatcher sameAs dispatcher
        capturedFlow sameAs internalFlow
    }
}
