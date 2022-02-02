/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termbox

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.MutableStateFlow
import tech.antibytes.wikidata.app.util.MwLocale

@Preview
@Composable
fun TermboxViewEditPreview() {
    TermboxEditView(
        onReadMode = { },
        viewModel = TermboxViewModelPreviewStub(
            MutableStateFlow("QTest"),
            MutableStateFlow(true),
            MutableStateFlow("Test"),
            MutableStateFlow("A Simple Preview"),
            MutableStateFlow(listOf("dummy", "preview", "Lorem ipsum")),
            MutableStateFlow(MwLocale("en"))
        )
    )
}
