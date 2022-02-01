/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termsearch

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tech.antibytes.wikibase.store.page.domain.model.EntityId
import tech.antibytes.wikibase.store.page.domain.model.LanguageTag
import tech.antibytes.wikibase.store.page.domain.model.PageModelContract

@Preview
@Composable
fun TermSearchScreenPreviewWithoutQueryAndResult() {
    TermSearchScreen(
        termSearchViewModel = TermSearchViewModelStub(
            MutableStateFlow(emptyList()),
            MutableStateFlow("")
        )
    )
}

@Preview
@Composable
fun TermSearchScreenPreviewWithQueryAndNoResult() {
    TermSearchScreen(
        termSearchViewModel = TermSearchViewModelStub(
            MutableStateFlow(emptyList()),
            MutableStateFlow("Test")
        )
    )
}

@Preview
@Composable
fun TermSearchScreenPreviewWithQueryAndResultWithoutLabelAndDescription() {
    TermSearchScreen(
        termSearchViewModel = TermSearchViewModelStub(
            MutableStateFlow(
                listOf(
                    SearchEntry()
                )
            ),
            MutableStateFlow("Test")
        )
    )
}

@Preview
@Composable
fun TermSearchScreenPreviewWithQueryAndResultWithLabel() {
    TermSearchScreen(
        termSearchViewModel = TermSearchViewModelStub(
            MutableStateFlow(
                listOf(
                    SearchEntry(
                        label = "test",
                    )
                )
            ),
            MutableStateFlow("Test")
        )
    )
}

@Preview
@Composable
fun TermSearchScreenPreviewWithQueryAndResultWithDescription() {
    TermSearchScreen(
        termSearchViewModel = TermSearchViewModelStub(
            MutableStateFlow(
                listOf(
                    SearchEntry(
                        description = "test",
                    )
                )
            ),
            MutableStateFlow("Test")
        )
    )
}

private class TermSearchViewModelStub(
    override val result: StateFlow<List<PageModelContract.SearchEntry>>,
    override val query: StateFlow<String>

) : TermSearchContract.TermSearchViewModel {
    override fun setQuery(query: String) { }

    override fun search() { }
}

private data class SearchEntry(
    override val id: EntityId = "test",
    override val language: LanguageTag = "de",
    override val label: String? = null,
    override val description: String? = null
) : PageModelContract.SearchEntry
