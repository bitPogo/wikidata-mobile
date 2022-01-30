/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termsearch

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.antibytes.wikibase.store.page.domain.model.PageModelContract
import tech.antibytes.wikidata.app.R
import tech.antibytes.wikidata.app.extension.useResourceOnNullOrBlank

@Composable
fun TermSearchScreen(viewModel: TermSearchContract.TermSearchViewModel) {
    val query = viewModel.query.collectAsState()
    val result = viewModel.result.collectAsState()
    val message = if (query.value.isEmpty()) {
        R.string.termsearch_missing_query
    } else {
        R.string.termsearch_no_results
    }
    
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
        ) {
            TermSearchBar(
                value = query.value,
                onValueChange = { newQuery ->
                    viewModel.setQuery(newQuery)
                },
                onSearch = { }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            if (result.value.isNotEmpty()) {
                LazyColumn {
                    items(result.value) { entry: PageModelContract.SearchEntry ->
                        TermSearchItem(
                            id = entry.id,
                            label = entry.label.useResourceOnNullOrBlank(R.string.termbox_missing_label),
                            description = entry.description.useResourceOnNullOrBlank(R.string.termbox_missing_description),
                            onClick = {}
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
    }
}
