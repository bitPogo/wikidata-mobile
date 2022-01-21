/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.page

import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract
import tech.antibytes.wikibase.store.database.page.PageQueries
import tech.antibytes.wikibase.store.page.domain.model.LanguageTag
import tech.antibytes.wikibase.store.page.domain.model.PageModelContract

interface PageStoreContract {
    interface PageStore {
        val randomItemId: CoroutineWrapperContract.SharedFlowWrapper<String, Exception>
        val searchEntries: CoroutineWrapperContract.SharedFlowWrapper<List<PageModelContract.SearchEntry>, Exception>

        fun fetchRandomItem()
        fun searchItems(term: String, language: LanguageTag)
    }

    interface PageStoreFactory {
        fun getInstance(
            client: PublicApi.Client,
            database: PageQueries,
            producerScope: CoroutineWrapperContract.CoroutineScopeDispatcher,
            consumerScope: CoroutineWrapperContract.CoroutineScopeDispatcher
        ): PageStore
    }
}
