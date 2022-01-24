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
fun SingleLineEditableTextFieldWithoutError() {
    SingleLineEditableTextField(
        label = "test",
        value = "myTest",
        {}
    )
}

@Preview
@Composable
fun DefaultSingleLineEditableTextFieldWithError() {
    SingleLineEditableTextField(
        label = "test",
        value = "myTest",
        {},
        isError = true
    )
}
