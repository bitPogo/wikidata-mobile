/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termbox

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import tech.antibytes.wikidata.app.R
import tech.antibytes.wikidata.app.extension.useResourceOnNullOrBlank
import tech.antibytes.wikidata.app.ui.molecule.ScreenWithTopBar

@Composable
fun TermboxView(
    onEditMode: () -> Unit,
    viewModel: TermboxContract.TermboxViewModel
) {
    val title = viewModel.id.collectAsState()
    val label = viewModel.label.collectAsState()
    val description = viewModel.description.collectAsState()
    val aliases = viewModel.aliases.collectAsState()
    val isEditable = viewModel.isEditable.collectAsState()

    ScreenWithTopBar(
        topBar = @Composable {
            TermMenu(
                title = title.value,
                isEditable = isEditable.value,
                onSearch = { /*TODO*/ },
                onEdit = onEditMode,
                onLanguageSearch = { /*TODO*/ },
                onRandomEntity = { viewModel.randomItem() }
            )
        },
        content = @Composable {
            TermView(
                label = label.value.useResourceOnNullOrBlank(R.string.termbox_missing_label),
                description = description.value.useResourceOnNullOrBlank(R.string.termbox_missing_description),
                aliases = aliases.value
            )
        }
    )
}
