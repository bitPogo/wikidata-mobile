/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.page

import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract
import tech.antibytes.wikibase.store.page.domain.model.LanguageTag
import tech.antibytes.wikibase.store.page.domain.model.PageModelContract

interface PageStoreContract {
    interface PageStore {
        val randomPage: CoroutineWrapperContract.SharedFlowWrapper<String, Exception>
        val search: CoroutineWrapperContract.SharedFlowWrapper<List<PageModelContract.SearchEntry>, Exception>

        fun fetchRandomPage()
        fun searchForItem(term: String, language: LanguageTag)
    }
}
