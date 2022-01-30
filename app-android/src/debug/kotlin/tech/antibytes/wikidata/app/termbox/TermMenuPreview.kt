/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termbox

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun DefaultTermboxMenuWithEnabledEditButton() {
    TermMenu(
        "QPreview",
        true,
        {},
        {},
        {},
        {}
    )
}

@Preview
@Composable
fun DefaultTermboxMenuWithDisabledEditButton() {
    TermMenu(
        "QPreview",
        true,
        {},
        {},
        {},
        {}
    )
}
