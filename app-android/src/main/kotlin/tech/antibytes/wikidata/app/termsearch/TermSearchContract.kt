/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termsearch

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import tech.antibytes.wikibase.store.page.domain.model.PageModelContract
import java.util.Locale

interface TermSearchContract {
    interface TermSearchViewModel {
        val result: SharedFlow<List<PageModelContract.SearchEntry>>
        val query: StateFlow<String>

        fun setQuery(query: String)
        fun search()
    }
}
