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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import tech.antibytes.wikidata.app.R
import tech.antibytes.wikidata.app.ui.theme.Blue
import tech.antibytes.wikidata.app.ui.theme.White

@Composable
private fun ActionsButtons(
    isEditable: Boolean,
    onSearch: () -> Unit,
    onEdit: () -> Unit,
    onRefresh: () -> Unit,
    onLanguageSearch: () -> Unit,
    onRandomEntity: () -> Unit
) {
    val expanded = remember { mutableStateOf(false) }
    val moreActionsDescriptor = if (expanded.value) {
        R.string.termbox_menu_show_less
    } else {
        R.string.termbox_menu_show_more
    }

    Row {
        IconButton(onClick = onSearch) {
            Icon(
                Icons.Outlined.Search,
                stringResource(R.string.termbox_menu_search_entity)
            )
        }

        IconButton(
            onClick = onRefresh
        ) {
            Icon(
                Icons.Outlined.Refresh,
                stringResource(R.string.termbox_menu_refresh)
            )
        }

        IconButton(
            onClick = onEdit,
            enabled = isEditable
        ) {
            Icon(
                Icons.Outlined.Edit,
                stringResource(R.string.termbox_menu_edit)
            )
        }

        Box(
            Modifier.wrapContentSize(Alignment.TopEnd)
        ) {
            IconButton(
                onClick = { expanded.value = true }
            ) {
                Icon(
                    Icons.Filled.MoreVert,
                    contentDescription = stringResource(moreActionsDescriptor)
                )
            }

            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false },
            ) {
                DropdownMenuItem(
                    onClick = {
                        expanded.value = false
                        onLanguageSearch.invoke()
                    }
                ) {
                    Text(stringResource(R.string.termbox_menu_search_language))
                }

                Divider()

                DropdownMenuItem(
                    onClick = {
                        expanded.value = false
                        onRandomEntity.invoke()
                    }
                ) {
                    Text(stringResource(R.string.termbox_menu_random_entity))
                }
            }
        }
    }
}

@Composable
fun TermMenu(
    title: String,
    isEditable: Boolean,
    onSearch: () -> Unit,
    onEdit: () -> Unit,
    onRefresh: () -> Unit,
    onLanguageSearch: () -> Unit,
    onRandomEntity: () -> Unit
) {
    TopAppBar(
        title = @Composable { Text(text = title) },
        backgroundColor = Blue,
        contentColor = White,
        modifier = Modifier.fillMaxWidth(),
        actions = @Composable {
            ActionsButtons(
                isEditable = isEditable,
                onSearch = onSearch,
                onRefresh = onRefresh,
                onEdit = onEdit,
                onLanguageSearch = onLanguageSearch,
                onRandomEntity = onRandomEntity
            )
        }
    )
}
