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
fun TermEditorPreviewShort() {
    TermEditor(
        label = "Something",
        description = "ShortDescription",
        aliases = listOf(""),
        onLabelInput = {},
        onDescriptionInput = {},
        onAliasInput = { _, _ -> Unit }
    )
}

@Preview
@Composable
fun TermEditorPreviewHuge() {
    TermEditor(
        label = "Something",
        description = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.",
        aliases = listOf("Lorem", "ipsum", "dolor", "sit", "amet"),
        onLabelInput = {},
        onDescriptionInput = {},
        onAliasInput = { _, _ -> Unit }
    )
}
