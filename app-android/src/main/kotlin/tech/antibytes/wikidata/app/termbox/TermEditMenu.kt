/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termbox

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import tech.antibytes.wikidata.app.R
import tech.antibytes.wikidata.app.ui.theme.Blue
import tech.antibytes.wikidata.app.ui.theme.BrightWhite

@Composable
fun TermEditMenu(
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    TopAppBar(
        title = @Composable { Text(text = "") },
        backgroundColor = Blue,
        contentColor = BrightWhite,
        modifier = Modifier.fillMaxWidth(),
        navigationIcon = @Composable {
            IconButton(onClick = onCancel) {
                Icon(
                    Icons.Default.Cancel,
                    stringResource(R.string.termbox_edit_cancel)
                )
            }
        },
        actions = @Composable {
            IconButton(onClick = onSave) {
                Icon(
                    Icons.Default.Done,
                    stringResource(R.string.termbox_edit_save)
                )
            }
        }
    )
}
