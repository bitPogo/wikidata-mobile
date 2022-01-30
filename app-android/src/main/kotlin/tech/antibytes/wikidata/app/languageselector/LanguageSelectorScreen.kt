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
import tech.antibytes.wikidata.app.ui.molecule.ScreenWithTopBar
import java.util.Locale

@Composable
fun LanguageSelectorScreen(viewModel: LanguageSelectorContract.LanguageSelectorViewModel,) {
    val languageQuery = viewModel.filter.collectAsState()
    val selectedLanguage = viewModel.currentLanguage.collectAsState()
    val selectableLanguages = viewModel.selection.collectAsState()

    ScreenWithTopBar(
        topBar = @Composable {
            LanguageSearchBar(
                value = languageQuery.value,
                onValueChange = { filter ->
                    viewModel.setFilter(filter)
                }
            )
        },
        content = @Composable {
            LazyColumn {
                itemsIndexed(selectableLanguages.value) { idx: Int, language: Locale ->
                    LanguageItem(
                        id = idx,
                        value = language,
                        selected = selectedLanguage.value,
                        onClick = { selector ->
                            viewModel.selectLanguage(selector)
                        }
                    )
                }
            }
        }
    )
}
