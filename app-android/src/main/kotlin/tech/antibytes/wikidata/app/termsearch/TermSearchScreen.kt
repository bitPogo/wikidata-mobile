/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termsearch

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import tech.antibytes.wikibase.store.page.domain.model.PageModelContract
import tech.antibytes.wikidata.app.R
import tech.antibytes.wikidata.app.extension.useResourceOnNullOrBlank
import tech.antibytes.wikidata.app.ui.molecule.ScreenWithTopBar

@Composable
fun TermSearchScreen(
    navigator: TermSearchContract.Navigator = TermSearchContract.Navigator { },
    termSearchViewModel: TermSearchContract.TermSearchViewModel = hiltViewModel<TermSearchViewModel>()
) {
    val query = termSearchViewModel.query.collectAsState()
    val result = termSearchViewModel.result.collectAsState()
    var hasActiveSearch by remember { mutableStateOf(false) }

    val message = if (query.value.isNotEmpty() && hasActiveSearch) {
        R.string.termsearch_no_results
    } else {
        R.string.termsearch_missing_query
    }

    ScreenWithTopBar(
        topBar = @Composable {
            TermSearchBar(
                value = query.value,
                onValueChange = termSearchViewModel::setQuery,
                onSearch = {
                    termSearchViewModel.search()
                    hasActiveSearch = true
                }
            )
        },
        content = @Composable {
            if (result.value.isNotEmpty()) {
                LazyColumn {
                    itemsIndexed(result.value) { idx, entry: PageModelContract.SearchEntry ->
                        TermSearchItem(
                            label = entry.label.useResourceOnNullOrBlank(R.string.termbox_missing_label),
                            description = entry.description.useResourceOnNullOrBlank(R.string.termbox_missing_description),
                            onClick = {
                                termSearchViewModel.select(idx)
                                hasActiveSearch = false
                                navigator.goToTermbox()
                            }
                        )
                    }
                }
            } else {
                Text(
                    stringResource(message),
                    textAlign = TextAlign.Center,
                    fontStyle = FontStyle.Italic,
                    fontSize = 17.sp,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth()
                )
            }
        }
    )
}
