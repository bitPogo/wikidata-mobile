/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.mock

import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError
import tech.antibytes.wikibase.store.page.PageStoreContract
import tech.antibytes.wikibase.store.page.domain.model.LanguageTag
import tech.antibytes.wikibase.store.page.domain.model.PageModelContract

class PageStoreStub(
    override val randomItemId: CoroutineWrapperContract.SharedFlowWrapper<String, Exception>,
    override val searchEntries: CoroutineWrapperContract.SharedFlowWrapper<List<PageModelContract.SearchEntry>, Exception>,
    var fetchRandomItem: (() -> Unit)? = null,
    var searchItems: ((String, LanguageTag) -> Unit)? = null
) : PageStoreContract.PageStore, MockContract.Mock {
    override fun fetchRandomItem() {
        return fetchRandomItem?.invoke()
            ?: throw MockError.MissingStub("Missing Stub fetchRandomItem")
    }

    override fun searchItems(term: String, language: LanguageTag) {
        return searchItems?.invoke(term, language)
            ?: throw MockError.MissingStub("Missing Stub searchItems")
    }

    override fun clear() {
        fetchRandomItem = null
        searchItems = null
    }
}
