/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termbox

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Locale

@Preview
@Composable
fun TermboxScreenPreview() {
    TermboxScreen(
        navigator = TermboxNavigatorPreviewStub(),
        termboxViewModel = TermboxViewModelPreviewStub(
            MutableStateFlow("QTest"),
            MutableStateFlow(false),
            MutableStateFlow("Test"),
            MutableStateFlow("A Simple Preview"),
            MutableStateFlow(emptyList()),
            MutableStateFlow(Locale.ENGLISH)
        )
    )
}
