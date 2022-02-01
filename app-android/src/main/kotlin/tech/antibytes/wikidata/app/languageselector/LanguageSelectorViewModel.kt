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
import java.util.Locale
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class LanguageSelectorViewModel @Inject constructor(
    @Named("MutableLanguageHandle") private val languageState: MutableStateFlow<Locale>,
    private val supportedLanguages: List<Locale>,
    private val entityStore: EntityStoreContract.EntityStore
) : LanguageSelectorContract.LanguageSelectorViewModel, ViewModel() {
    override val currentLanguage: StateFlow<Locale> = languageState

    private val _selection: MutableStateFlow<List<Locale>> = MutableStateFlow(supportedLanguages)
    override val selection: StateFlow<List<Locale>> = _selection

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

    private fun applyFilter(filter: String): List<Locale> {
        return supportedLanguages.filter { locale ->
            locale.displayLanguage
                .lowercase()
                .contains(filter)
        }
    }

    private fun filterSelection(filter: String): List<Locale> {
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

    private fun getLanguageTag(language: Locale): String {
        return language.toLanguageTag().replace('_', '-')
    }

    override fun selectLanguage(selector: Int) {
        languageState.update { _selection.value[selector] }
        entityStore.fetchEntity(
            id.value,
            getLanguageTag(_selection.value[selector])
        )
    }
}
