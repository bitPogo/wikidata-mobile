/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.languageselector

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.antibytes.wikibase.store.entity.EntityStoreContract
import tech.antibytes.wikidata.app.ApplicationContract
import tech.antibytes.wikidata.app.di.MutableLanguageHandle
import tech.antibytes.wikidata.app.di.SupportedLanguages
import tech.antibytes.wikidata.app.util.UtilContract.MwLocale
import javax.inject.Inject

@HiltViewModel
class LanguageSelectorViewModel @Inject constructor(
    @MutableLanguageHandle private val languageState: @JvmSuppressWildcards(true) MutableStateFlow<MwLocale>,
    @SupportedLanguages private val supportedLanguages: @JvmSuppressWildcards(true) List<MwLocale>,
    private val entityStore: EntityStoreContract.EntityStore
) : LanguageSelectorContract.LanguageSelectorViewModel, ViewModel() {
    override val currentLanguage: StateFlow<MwLocale> = languageState

    private val _selection: MutableStateFlow<List<MwLocale>> = MutableStateFlow(supportedLanguages)
    override val selection: StateFlow<List<MwLocale>> = _selection

    private val _filter: MutableStateFlow<String> = MutableStateFlow("")
    override val filter: StateFlow<String> = _filter

    private val id = MutableStateFlow("")

    init {
        entityStore.entity.subscribeWithSuspendingFunction { entity ->
            if (entity.isSuccess()) {
                id.update { entity.unwrap().id }
            } else {
                Log.d(
                    ApplicationContract.LogTag.LANGUAGE_SELECTOR_VIEWMODEL.value,
                    entity.error?.message ?: entity.error.toString()
                )
            }
        }
    }

    private fun applyFilter(filter: String): List<MwLocale> {
        return supportedLanguages.filter { locale ->
            locale.displayName.lowercase().contains(filter)
        }
    }

    private fun filterSelection(filter: String): List<MwLocale> {
        val normalizedFilter = filter.lowercase()

        return if (normalizedFilter.isEmpty()) {
            supportedLanguages
        } else {
            applyFilter(normalizedFilter)
        }
    }

    override fun setFilter(newFilter: String) {
        viewModelScope.launch {
            _filter.update { newFilter }
            _selection.update { filterSelection(newFilter) }
        }
    }

    override fun selectLanguage(selector: Int) {
        languageState.update { _selection.value[selector] }
        entityStore.fetchEntity(
            id.value,
            _selection.value[selector].toLanguageTag()
        )
    }
}
