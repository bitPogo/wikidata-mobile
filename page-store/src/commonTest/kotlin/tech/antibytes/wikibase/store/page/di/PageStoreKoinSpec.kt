/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.page.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import tech.antibytes.util.coroutine.result.ResultContract
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.CoroutineScopeDispatcher
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.SharedFlowWrapper
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.SharedFlowWrapperFactory
import tech.antibytes.util.test.isNot
import tech.antibytes.util.test.sameAs
import tech.antibytes.wikibase.store.mock.SharedFlowWrapperFactoryStub
import tech.antibytes.wikibase.store.mock.SharedFlowWrapperStub
import tech.antibytes.wikibase.store.page.domain.DomainContract
import tech.antibytes.wikibase.store.page.domain.model.EntityId
import tech.antibytes.wikibase.store.page.domain.model.PageModelContract
import kotlin.test.Test

class PageStoreKoinSpec {
    @Test
    fun `Given resolvePageStoreModule is called, it contains MutableSharedFlow for RandomPageIds`() {
        // Given
        val koin = koinApplication {
            modules(
                resolvePageStoreModule()
            )
        }

        // When
        val flow: MutableSharedFlow<ResultContract<EntityId, Exception>> = koin.koin.get(
            named(DomainContract.DomainKoinIds.INTERNAL_RANDOM_FLOW)
        )

        // Then
        flow isNot null
    }

    @Test
    fun `Given resolvePageStoreModule is called, it contains MutableSharedFlow for RandomPageIds, while not creating it twice`() {
        // Given
        val koin = koinApplication {
            modules(
                resolvePageStoreModule()
            )
        }

        // When
        val flow1: MutableSharedFlow<ResultContract<EntityId, Exception>> = koin.koin.get(
            named(DomainContract.DomainKoinIds.INTERNAL_RANDOM_FLOW)
        )
        val flow2: MutableSharedFlow<ResultContract<EntityId, Exception>> = koin.koin.get(
            named(DomainContract.DomainKoinIds.INTERNAL_RANDOM_FLOW)
        )
        // Then
        flow1 sameAs flow2
    }

    @Test
    fun `Given resolvePageStoreModule is called, it contains MutableSharedFlow for ItemSearches`() {
        // Given
        val koin = koinApplication {
            modules(
                resolvePageStoreModule()
            )
        }

        // When
        val flow: MutableSharedFlow<ResultContract<EntityId, Exception>> = koin.koin.get(
            named(DomainContract.DomainKoinIds.INTERNAL_SEARCH_FLOW)
        )

        // Then
        flow isNot null
    }

    @Test
    fun `Given resolvePageStoreModule is called, it contains MutableSharedFlow for ItemSearches, while not creating it twice`() {
        // Given
        val koin = koinApplication {
            modules(
                resolvePageStoreModule()
            )
        }

        // When
        val flow1: MutableSharedFlow<ResultContract<EntityId, Exception>> = koin.koin.get(
            named(DomainContract.DomainKoinIds.INTERNAL_SEARCH_FLOW)
        )
        val flow2: MutableSharedFlow<ResultContract<EntityId, Exception>> = koin.koin.get(
            named(DomainContract.DomainKoinIds.INTERNAL_SEARCH_FLOW)
        )
        // Then
        flow1 sameAs flow2
    }

    @Test
    fun `Given resolvePageStoreModule is called, it contains SharedFlowWrapperFactory`() {
        // Given
        val koin = koinApplication {
            modules(
                resolvePageStoreModule()
            )
        }

        // When
        val factory: SharedFlowWrapperFactory = koin.koin.get()

        // Then
        factory isNot null
    }

    @Test
    fun `Given resolvePageStoreModule is called, it creates a SharedFlowWrapper for RandomPageIds`() {
        // Given
        val koin = koinApplication {
            modules(
                resolvePageStoreModule(),
                module {
                    single<CoroutineScopeDispatcher>(named(DomainContract.DomainKoinIds.CONSUMER_SCOPE)) {
                        CoroutineScopeDispatcher { CoroutineScope(Dispatchers.Default) }
                    }
                }
            )
        }

        // When
        val flow: SharedFlowWrapper<EntityId, Exception> = koin.koin.get(
            named(DomainContract.DomainKoinIds.EXTERNAL_RANDOM_FLOW)
        )

        // Then
        flow isNot null
    }

    @Test
    fun `Given resolvePageStoreModule is called, it contains SharedFlowWrapper for RandomPageIds`() {
        // Given
        val dispatcher = CoroutineScopeDispatcher { CoroutineScope(Dispatchers.Default) }
        val internalFlow = MutableSharedFlow<ResultContract<EntityId, Exception>>()

        var capturedFlow: SharedFlow<ResultContract<EntityId, Exception>>? = null
        var capturedDispatcher: CoroutineScopeDispatcher? = null
        val expected = SharedFlowWrapperStub<EntityId, Exception>()

        val factory = SharedFlowWrapperFactoryStub<EntityId, Exception> { givenFlow, givenDispatcher ->
            capturedFlow = givenFlow
            capturedDispatcher = givenDispatcher

            expected
        }

        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolvePageStoreModule(),
                module {
                    single<SharedFlowWrapperFactory> {
                        factory
                    }

                    single(named(DomainContract.DomainKoinIds.CONSUMER_SCOPE)) { dispatcher }

                    single(
                        named(DomainContract.DomainKoinIds.INTERNAL_RANDOM_FLOW)
                    ) {
                        internalFlow
                    }
                }
            )
        }

        // When
        val flow: SharedFlowWrapper<EntityId, Exception> = koin.koin.get(
            named(DomainContract.DomainKoinIds.EXTERNAL_RANDOM_FLOW)
        )

        // Then
        flow sameAs expected
        capturedDispatcher sameAs dispatcher
        capturedFlow sameAs internalFlow
    }

    @Test
    fun `Given resolvePageStoreModule is called, it creates a SharedFlowWrapper for ItemSearches`() {
        // Given
        val koin = koinApplication {
            modules(
                resolvePageStoreModule(),
                module {
                    single<CoroutineScopeDispatcher>(named(DomainContract.DomainKoinIds.CONSUMER_SCOPE)) {
                        CoroutineScopeDispatcher { CoroutineScope(Dispatchers.Default) }
                    }
                }
            )
        }

        // When
        val flow: SharedFlowWrapper<List<PageModelContract.SearchEntry>, Exception> = koin.koin.get(
            named(DomainContract.DomainKoinIds.EXTERNAL_SEARCH_FLOW)
        )

        // Then
        flow isNot null
    }

    @Test
    fun `Given resolvePageStoreModule is called, it contains SharedFlowWrapper for ItemSearches`() {
        // Given
        val dispatcher = CoroutineScopeDispatcher { CoroutineScope(Dispatchers.Default) }
        val internalFlow = MutableSharedFlow<ResultContract<List<PageModelContract.SearchEntry>, Exception>>()

        var capturedFlow: SharedFlow<ResultContract<List<PageModelContract.SearchEntry>, Exception>>? = null
        var capturedDispatcher: CoroutineScopeDispatcher? = null
        val expected = SharedFlowWrapperStub<List<PageModelContract.SearchEntry>, Exception>()

        val factory = SharedFlowWrapperFactoryStub<List<PageModelContract.SearchEntry>, Exception> { givenFlow, givenDispatcher ->
            capturedFlow = givenFlow
            capturedDispatcher = givenDispatcher

            expected
        }

        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolvePageStoreModule(),
                module {
                    single<SharedFlowWrapperFactory> {
                        factory
                    }

                    single(named(DomainContract.DomainKoinIds.CONSUMER_SCOPE)) { dispatcher }

                    single(
                        named(DomainContract.DomainKoinIds.INTERNAL_SEARCH_FLOW)
                    ) {
                        internalFlow
                    }
                }
            )
        }

        // When
        val flow: SharedFlowWrapper<List<PageModelContract.SearchEntry>, Exception> = koin.koin.get(
            named(DomainContract.DomainKoinIds.EXTERNAL_SEARCH_FLOW)
        )

        // Then
        flow sameAs expected
        capturedDispatcher sameAs dispatcher
        capturedFlow sameAs internalFlow
    }
}
