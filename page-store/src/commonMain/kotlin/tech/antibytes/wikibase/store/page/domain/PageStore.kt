/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.page.domain

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import org.koin.core.KoinApplication
import org.koin.core.qualifier.named
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.util.coroutine.result.Failure
import tech.antibytes.util.coroutine.result.ResultContract
import tech.antibytes.util.coroutine.result.Success
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract
import tech.antibytes.wikibase.store.database.page.PageQueries
import tech.antibytes.wikibase.store.page.PageStoreContract
import tech.antibytes.wikibase.store.page.di.initKoin
import tech.antibytes.wikibase.store.page.domain.model.EntityId
import tech.antibytes.wikibase.store.page.domain.model.LanguageTag
import tech.antibytes.wikibase.store.page.domain.model.PageModelContract

class PageStore internal constructor(
    koin: KoinApplication
) : PageStoreContract.PageStore {
    private val randomIdFlow: MutableSharedFlow<ResultContract<EntityId, Exception>> by koin.koin.inject(
        named(DomainContract.DomainKoinIds.INTERNAL_RANDOM_FLOW)
    )

    override val randomPage: CoroutineWrapperContract.SharedFlowWrapper<String, Exception> = koin.koin.get(
        named(DomainContract.DomainKoinIds.EXTERNAL_RANDOM_FLOW)
    )

    private val searchFlow: MutableSharedFlow<ResultContract<List<PageModelContract.SearchEntry>, Exception>> by koin.koin.inject(
        named(DomainContract.DomainKoinIds.INTERNAL_SEARCH_FLOW)
    )

    override val search: CoroutineWrapperContract.SharedFlowWrapper<List<PageModelContract.SearchEntry>, Exception> = koin.koin.get(
        named(DomainContract.DomainKoinIds.EXTERNAL_SEARCH_FLOW)
    )

    private val dispatcher: CoroutineWrapperContract.CoroutineScopeDispatcher by koin.koin.inject(
        named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)
    )

    private val localRepository: DomainContract.LocalRepository by koin.koin.inject(
        named(DomainContract.DomainKoinIds.LOCAL)
    )

    private val remoteRepository: DomainContract.RemoteRepository by koin.koin.inject(
        named(DomainContract.DomainKoinIds.REMOTE)
    )

    private suspend fun <T : Any> wrapResult(
        action: suspend () -> T
    ): ResultContract<T, Exception> {
        return try {
            Success(action.invoke())
        } catch (error: Exception) {
            Failure(error)
        }
    }

    private fun <T : Any> executeEvent(
        flow: MutableSharedFlow<ResultContract<T, Exception>>,
        event: suspend () -> T
    ) {
        dispatcher.dispatch().launch {
            flow.emit(
                wrapResult(event)
            )
        }
    }

    private suspend fun fetchAndStoreRandomPageId(): EntityId {
        val ids = remoteRepository.fetchRandomItemIds()

        localRepository.saveRandomPageIds(
            ids.subList(1, ids.size)
        )

        return ids.first()
    }

    override fun fetchRandomItem() {
        executeEvent(randomIdFlow) {
            localRepository.fetchRandomPageId()
                ?: fetchAndStoreRandomPageId()
        }
    }

    override fun searchForItem(term: String, language: LanguageTag) {
        executeEvent(searchFlow) {
            remoteRepository.searchForItem(term, language)
        }
    }

    companion object : PageStoreContract.PageStoreFactory {
        override fun getInstance(
            client: PublicApi.Client,
            database: PageQueries,
            producerScope: CoroutineWrapperContract.CoroutineScopeDispatcher,
            consumerScope: CoroutineWrapperContract.CoroutineScopeDispatcher
        ): PageStoreContract.PageStore {
            val koin = initKoin(
                client,
                database,
                producerScope,
                consumerScope
            )

            return PageStore(koin)
        }
    }
}
