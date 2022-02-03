/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.languageselector

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import tech.antibytes.wikidata.app.ui.molecule.ScreenWithTopBar
import tech.antibytes.wikidata.app.util.UtilContract

@Composable
fun LanguageSelectorScreen(
    navigator: LanguageSelectorContract.Navigator = LanguageSelectorContract.Navigator { },
    languageSelectorViewModel: LanguageSelectorContract.LanguageSelectorViewModel = hiltViewModel<LanguageSelectorViewModel>()
) {
    val languageQuery = languageSelectorViewModel.filter.collectAsState()
    val selectedLanguage = languageSelectorViewModel.currentLanguage.collectAsState()
    val selectableLanguages = languageSelectorViewModel.selection.collectAsState()

    ScreenWithTopBar(
        topBar = @Composable {
            LanguageSearchBar(
                value = languageQuery.value,
                onValueChange = languageSelectorViewModel::setFilter
            )
        },
        content = @Composable {
            LazyColumn {
                itemsIndexed(selectableLanguages.value) { idx: Int, language: UtilContract.MwLocale ->
                    LanguageItem(
                        id = idx,
                        value = language,
                        selected = selectedLanguage.value,
                        onClick = { id ->
                            languageSelectorViewModel.selectLanguage(id)
                            navigator.goToTermbox()
                        }
                    )
                }
            }
        }
    )
}
