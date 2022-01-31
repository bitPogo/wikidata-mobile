/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termbox

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Locale.ENGLISH

@Preview
@Composable
fun TermboxViewPreview() {
    TermboxView(
        onEditMode = { },
        viewModel = TermboxViewModelPreviewStub(
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
        viewModel = TermboxViewModelPreviewStub(
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
        viewModel = TermboxViewModelPreviewStub(
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
        viewModel = TermboxViewModelPreviewStub(
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
        viewModel = TermboxViewModelPreviewStub(
            MutableStateFlow("QTest"),
            MutableStateFlow(false),
            MutableStateFlow("Test"),
            MutableStateFlow("A Simple Preview"),
            MutableStateFlow(emptyList()),
            MutableStateFlow(ENGLISH)
        )
    )
}
