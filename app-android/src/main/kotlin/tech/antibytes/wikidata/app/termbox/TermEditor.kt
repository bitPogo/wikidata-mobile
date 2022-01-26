/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termbox

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tech.antibytes.wikidata.app.R
import tech.antibytes.wikidata.app.ui.atom.AliasEditField
import tech.antibytes.wikidata.app.ui.atom.MultiLineEditableText
import tech.antibytes.wikidata.app.ui.atom.SingleLineEditableText

@Composable
fun TermEditor(
    label: String,
    description: String,
    aliases: List<String>,
    onLabelInput: (String) -> Unit,
    onDescriptionInput: (String) -> Unit,
    onAliasInput: (Int, String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 20.dp,
                bottom = 10.dp,
                start = 10.dp,
                end = 10.dp,
            )
            .fillMaxSize(1F)
    ) {
        item {
            SingleLineEditableText(
                stringResource(R.string.termbox_edit_label),
                label,
                onChange = onLabelInput,
                underlineIndicator = true
            )

            Spacer(modifier = Modifier.height(15.dp))


            MultiLineEditableText(
                stringResource(R.string.termbox_edit_description),
                description,
                onChange = onDescriptionInput,
                underlineIndicator = true,
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(stringResource(R.string.termbox_view_aka))
        }

        itemsIndexed(aliases) { idx, value ->
            val new = if (value.isEmpty()) {
                stringResource(R.string.termbox_edit_new_aka)
            } else {
                null
            }

            AliasEditField(
                label = new,
                value = value,
                onChange = { newValue ->
                    onAliasInput.invoke(idx, newValue)
                },
            )
        }
    }
}
