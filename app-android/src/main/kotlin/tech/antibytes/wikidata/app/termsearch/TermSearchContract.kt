/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termsearch

import kotlinx.coroutines.flow.StateFlow
import tech.antibytes.wikibase.store.page.domain.model.PageModelContract

interface TermSearchContract {
    interface TermSearchViewModel {
        val result: StateFlow<List<PageModelContract.SearchEntry>>
        val query: StateFlow<String>

        fun setQuery(query: String)
        fun search()

        fun select(index: Int)
    }

    fun interface Navigator {
        fun goToTermbox()
    }
}
