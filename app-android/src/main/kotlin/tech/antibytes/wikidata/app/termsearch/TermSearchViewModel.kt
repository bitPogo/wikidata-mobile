/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termsearch

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import tech.antibytes.wikibase.store.page.PageStoreContract
import tech.antibytes.wikibase.store.page.domain.model.PageModelContract
import java.util.Locale

class TermSearchViewModel(
    private val store: PageStoreContract.PageStore,
    private val currentLanguage: StateFlow<Locale>
) : TermSearchContract.TermSearchViewModel, ViewModel() {
    private val _result = MutableStateFlow<List<PageModelContract.SearchEntry>>(emptyList())
    override val result: StateFlow<List<PageModelContract.SearchEntry>> = _result

    private val _query = MutableStateFlow("")
    override val query: StateFlow<String> = _query

    init {
        store.searchEntries.subscribe { searchResult ->
            if (searchResult.isSuccess()) {
                _result.update { searchResult.unwrap() }
            }
        }
    }

    override fun setQuery(query: String) {
        _query.update { query }
    }

    override fun search() {
        store.searchItems(
            _query.value,
            currentLanguage.value.toLanguageTag().replace('_', '-')
        )
    }
}
