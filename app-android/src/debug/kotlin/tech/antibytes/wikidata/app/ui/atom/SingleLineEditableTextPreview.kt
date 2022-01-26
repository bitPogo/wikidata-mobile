/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.ui.atom

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun SingleLineEditableTextWithoutError() {
    SingleLineEditableText(
        label = "test",
        value = "myTest",
        {}
    )
}

@Preview
@Composable
fun DefaultSingleLineEditableTextWithError() {
    SingleLineEditableText(
        label = "test",
        value = "myTest",
        {},
        isError = true
    )
}

@Preview
@Composable
fun DefaultSingleLineEditableTextBorderless() {
    SingleLineEditableText(
        label = "test",
        value = "myTest",
        {},
        isError = true,
        underlineIndicator = true
    )
}
