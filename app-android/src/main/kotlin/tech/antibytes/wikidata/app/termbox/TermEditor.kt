/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termbox

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import tech.antibytes.wikidata.app.R
import tech.antibytes.wikidata.app.extension.focusItem
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
    onAliasInput: (Int, String) -> Unit,
    onNewAliasInput: (String) -> Unit,
    focusAlias: Int = -1
) {
    val keyboard = KeyboardOptions.Default.copy(
        imeAction = ImeAction.Next
    )
    val uiState = rememberLazyListState()

    LazyColumn(
        state = uiState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 20.dp,
                bottom = 10.dp,
                start = 10.dp,
                end = 10.dp,
            )
            .fillMaxHeight(5F)
    ) {
        item {
            SingleLineEditableText(
                stringResource(R.string.termbox_edit_label),
                label,
                onChange = onLabelInput,
                underlineIndicator = true,
                keyboardOptions = keyboard.copy()
            )

            Spacer(modifier = Modifier.height(15.dp))

            MultiLineEditableText(
                stringResource(R.string.termbox_edit_description),
                description,
                onChange = onDescriptionInput,
                underlineIndicator = true,
                keyboardOptions = keyboard.copy(),
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(stringResource(R.string.termbox_view_aka))
        }

        itemsIndexed(aliases) { idx, value ->
            AliasEditField(
                value = value,
                onChange = { newValue ->
                    onAliasInput.invoke(idx, newValue)
                },
                keyboardOptions = keyboard.copy(),
                modifier = {
                    if (focusAlias == idx) {
                        focusItem()
                    } else {
                        this
                    }
                },
            )
        }

        item {
            IconButton(
                onClick = { onNewAliasInput.invoke("") }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.termbox_edit_new_aka)
                )
            }
        }
    }
}
