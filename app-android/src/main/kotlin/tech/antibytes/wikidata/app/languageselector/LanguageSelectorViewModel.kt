/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.languageselector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class LanguageSelectorViewModel @Inject constructor(
    @Named("MutableLanguageHandle") private val languageState: MutableStateFlow<Locale>,
    private val supportedLanguages: List<Locale>,
) : LanguageSelectorContract.LanguageSelectorViewModel, ViewModel() {
    override val currentLanguage: StateFlow<Locale> = languageState

    private val _selection: MutableStateFlow<List<Locale>> = MutableStateFlow(supportedLanguages)
    override val selection: StateFlow<List<Locale>> = _selection

    private val _filter: MutableStateFlow<String> = MutableStateFlow("")
    override val filter: StateFlow<String> = _filter

    private fun applyFilter(filter: String): List<Locale> {
        return supportedLanguages.filter { locale ->
            locale.displayLanguage
                .lowercase(Locale.getDefault())
                .contains(filter)
        }
    }

    private fun filterSelection(filter: String): List<Locale> {
        val normalizedFilter = filter.lowercase(Locale.getDefault())

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
    }
}
