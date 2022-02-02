/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termsearch

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import tech.antibytes.wikibase.store.entity.EntityStoreContract
import tech.antibytes.wikibase.store.page.PageStoreContract
import tech.antibytes.wikibase.store.page.domain.model.PageModelContract
import tech.antibytes.wikidata.app.ApplicationContract
import tech.antibytes.wikidata.app.di.LanguageState
import tech.antibytes.wikidata.app.util.UtilContract.MwLocale
import javax.inject.Inject

@HiltViewModel
class TermSearchViewModel @Inject constructor(
    private val pageStore: PageStoreContract.PageStore,
    private val entityStore: EntityStoreContract.EntityStore,
    @LanguageState private val currentLanguage: @JvmSuppressWildcards(true) StateFlow<MwLocale>
) : TermSearchContract.TermSearchViewModel, ViewModel() {
    private val _result = MutableStateFlow<List<PageModelContract.SearchEntry>>(emptyList())
    override val result: StateFlow<List<PageModelContract.SearchEntry>> = _result

    private val _query = MutableStateFlow("")
    override val query: StateFlow<String> = _query

    init {
        pageStore.searchEntries.subscribe { searchResult ->
            if (searchResult.isSuccess()) {
                _result.update { searchResult.unwrap() }
            } else {
                Log.d(
                    ApplicationContract.LogTag.TERMSEARCH_VIEWMODEL.value,
                    searchResult.error?.message ?: searchResult.error.toString()
                )
            }
        }
    }

    override fun setQuery(query: String) {
        _query.update { query }
    }

    override fun search() {
        pageStore.searchItems(
            _query.value,
            currentLanguage.value.toLanguageTag()
        )
    }

    override fun select(index: Int) {
        val item = _result.value[index]

        entityStore.fetchEntity(
            item.id,
            currentLanguage.value.toLanguageTag()
        )
    }
}
