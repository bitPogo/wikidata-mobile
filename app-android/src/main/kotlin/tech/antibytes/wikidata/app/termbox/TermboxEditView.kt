/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termbox

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import tech.antibytes.wikidata.app.ui.molecule.ScreenWithTopBar

@Composable
fun TermboxEditView(
    onReadMode: () -> Unit,
    viewModel: TermboxContract.TermboxViewModel
) {
    val label = viewModel.label.collectAsState()
    val description = viewModel.description.collectAsState()
    val aliases = viewModel.aliases.collectAsState()

    var focus by remember { mutableStateOf(-1) }

    ScreenWithTopBar(
        topBar = @Composable {
            TermEditMenu(
                onCancel = {
                    viewModel.dischargeChanges()
                    onReadMode.invoke()
                },
                onSave = {
                    viewModel.saveChanges()
                    onReadMode.invoke()
                }
            )
        },
        content = @Composable {
            TermEditor(
                label = label.value,
                description = description.value,
                aliases = aliases.value,
                onLabelInput = viewModel::setLabel,
                onDescriptionInput = viewModel::setDescription,
                onAliasInput = viewModel::setAlias,
                onNewAliasInput = { newAlias ->
                    viewModel.addAlias(newAlias)
                    focus = aliases.value.size
                },
                focusAlias = focus
            )
        }
    )
}
