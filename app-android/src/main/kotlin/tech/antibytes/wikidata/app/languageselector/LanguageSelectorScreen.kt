/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.languageselector

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun LanguageSelectorScreen(viewModel: LanguageSelectorContract.LanguageSelectorViewModel, ) {
    val languageQuery = viewModel.filter.collectAsState()
    val selectedLanguage = viewModel.currentLanguage.collectAsState()
    val selectableLanguages = viewModel.selection.collectAsState()

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
        ) {
            LanguageSearchBar(
                value = languageQuery.value,
                onValueChange = { filter ->
                    viewModel.setFilter(filter)
                }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
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
    }
}
