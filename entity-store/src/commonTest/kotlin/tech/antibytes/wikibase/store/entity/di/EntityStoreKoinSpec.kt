/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import tech.antibytes.util.coroutine.result.ResultContract
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.CoroutineScopeDispatcher
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.SharedFlowWrapperFactory
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.SharedFlowWrapper
import tech.antibytes.util.test.isNot
import tech.antibytes.util.test.sameAs
import tech.antibytes.wikibase.store.entity.domain.DomainContract
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract
import tech.antibytes.wikibase.store.mock.SharedFlowWrapperFactoryStub
import tech.antibytes.wikibase.store.mock.SharedFlowWrapperStub
import kotlin.test.Test

class EntityStoreKoinSpec {
    @Test
    fun `Given resolveEntityStoreModule is called, it contains MutableSharedFlow`() {
        // Given
        val koin = koinApplication {
            modules(
                resolveEntityStoreModule()
            )
        }

        // When
        val flow: MutableSharedFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>> = koin.koin.get()

        // Then
        flow isNot null
    }

    @Test
    fun `Given resolveEntityStoreModule is called, it contains MutableSharedFlow, while not creating it twice`() {
        // Given
        val koin = koinApplication {
            modules(
                resolveEntityStoreModule()
            )
        }

        // When
        val flow1: MutableSharedFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>> = koin.koin.get()
        val flow2: MutableSharedFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>> = koin.koin.get()

        // Then
        flow1 sameAs flow2
    }

    @Test
    fun `Given resolveEntityStoreModule is called, it contains SharedFlowWrapperFactory`() {
        // Given
        val koin = koinApplication {
            modules(
                resolveEntityStoreModule()
            )
        }

        // When
        val factory: SharedFlowWrapperFactory = koin.koin.get()

        // Then
        factory isNot null
    }

    @Test
    fun `Given resolveEntityStoreModule is called, it creates a SharedFlowWrapper`() {
        // Given
        val koin = koinApplication {
            modules(
                resolveEntityStoreModule(),
                module {
                    single<CoroutineScopeDispatcher>(named(DomainContract.DomainKoinIds.CONSUMER_SCOPE)) {
                        CoroutineScopeDispatcher { CoroutineScope(Dispatchers.Default) }
                    }

                }
            )
        }

        // When
        val flow: SharedFlowWrapper<EntityModelContract.MonolingualEntity, Exception> = koin.koin.get()

        // Then
        flow isNot null
    }

    @Test
    fun `Given resolveEntityStoreModule is called, it contains SharedFlowWrapper`() {
        // Given
        val dispatcher = CoroutineScopeDispatcher { CoroutineScope(Dispatchers.Default) }
        val internalFlow = MutableSharedFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>>()

        var capturedFlow: SharedFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>>? = null
        var capturedDispatcher: CoroutineScopeDispatcher? = null
        val expected = SharedFlowWrapperStub<EntityModelContract.MonolingualEntity, Exception>()

        val factory = SharedFlowWrapperFactoryStub<EntityModelContract.MonolingualEntity, Exception> { givenFlow, givenDispatcher ->
            capturedFlow = givenFlow
            capturedDispatcher = givenDispatcher

            expected
        }

        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolveEntityStoreModule(),
                module {
                    single<SharedFlowWrapperFactory> {
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
        val flow: SharedFlowWrapper<EntityModelContract.MonolingualEntity, Exception> = koin.koin.get()

        // Then
        flow sameAs expected
        capturedDispatcher sameAs dispatcher
        capturedFlow sameAs internalFlow
    }
}
