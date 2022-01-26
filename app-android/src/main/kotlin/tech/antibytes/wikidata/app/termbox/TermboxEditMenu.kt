/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termbox

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import tech.antibytes.wikidata.app.R
import tech.antibytes.wikidata.app.ui.theme.Blue
import tech.antibytes.wikidata.app.ui.theme.BrightWhite

@Composable
fun TermboxEditMenu(
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
