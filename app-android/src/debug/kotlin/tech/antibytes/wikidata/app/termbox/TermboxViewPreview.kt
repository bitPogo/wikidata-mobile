/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termbox

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale
import java.util.Locale.ENGLISH

@Preview
@Composable
fun TermboxViewPreview() {
    TermboxView(
        onEditMode = { },
        viewModel = TermboxViewModelStub(
            MutableStateFlow("QTest"),
            MutableStateFlow(true),
            MutableStateFlow("Test"),
            MutableStateFlow("A Simple Preview"),
            MutableStateFlow(listOf("dummy", "preview", "Lorem ipsum")),
            MutableStateFlow(ENGLISH)
        )
    )
}

@Preview
@Composable
fun TermboxViewPreviewWithEmptyLabel() {
    TermboxView(
        onEditMode = { },
        viewModel = TermboxViewModelStub(
            MutableStateFlow("QTest"),
            MutableStateFlow(true),
            MutableStateFlow(""),
            MutableStateFlow("A Simple Preview"),
            MutableStateFlow(listOf("dummy", "preview", "Lorem ipsum")),
            MutableStateFlow(ENGLISH)
        )
    )
}

@Preview
@Composable
fun TermboxViewPreviewWithEmptyDescription() {
    TermboxView(
        onEditMode = { },
        viewModel = TermboxViewModelStub(
            MutableStateFlow("QTest"),
            MutableStateFlow(true),
            MutableStateFlow("Test"),
            MutableStateFlow(""),
            MutableStateFlow(listOf("dummy", "preview", "Lorem ipsum")),
            MutableStateFlow(ENGLISH)
        )
    )
}

@Preview
@Composable
fun TermboxViewPreviewWithEmptyAliases() {
    TermboxView(
        onEditMode = { },
        viewModel = TermboxViewModelStub(
            MutableStateFlow("QTest"),
            MutableStateFlow(true),
            MutableStateFlow("Test"),
            MutableStateFlow("A Simple Preview"),
            MutableStateFlow(emptyList()),
            MutableStateFlow(ENGLISH)
        )
    )
}

@Preview
@Composable
fun TermboxViewPreviewProtected() {
    TermboxView(
        onEditMode = { },
        viewModel = TermboxViewModelStub(
            MutableStateFlow("QTest"),
            MutableStateFlow(false),
            MutableStateFlow("Test"),
            MutableStateFlow("A Simple Preview"),
            MutableStateFlow(emptyList()),
            MutableStateFlow(ENGLISH)
        )
    )
}

private class TermboxViewModelStub(
    override val id: StateFlow<String>,
    override val isEditable: StateFlow<Boolean>,
    override val label: StateFlow<String>,
    override val description: StateFlow<String>,
    override val aliases: StateFlow<List<String>>,
    override val language: StateFlow<Locale>
) : TermboxContract.TermboxViewModel {
    override fun setLabel(newLabel: String) {
    }

    override fun setDescription(newDescription: String) {
    }

    override fun setAlias(idx: Int, newAlias: String) {
    }

    override fun dischargeChanges() {
    }

    override fun saveChanges() {
    }

    override fun refresh() {
    }

    override fun createNewItem() {
    }

    override fun randomItem() {
    }

    override fun fetchItem(id: String) {
    }
}
