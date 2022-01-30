/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termbox

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import tech.antibytes.wikidata.app.ui.molecule.ScreenWithTopBar

@Composable
fun TermboxEditView(
    onReadMode: () -> Unit,
    viewModel: TermboxContract.TermboxViewModel
) {
    val label = viewModel.label.collectAsState()
    val description = viewModel.description.collectAsState()
    val aliases = viewModel.aliases.collectAsState()

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
                onLabelInput = { newLabel ->
                    viewModel.setLabel(newLabel)
                },
                onDescriptionInput = { newDescription ->
                    viewModel.setDescription(newDescription)
                },
                onAliasInput = { idx, alias ->
                    viewModel.setAlias(idx, alias)
                }
            )
        }
    )
}
