/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termbox

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun TermboxScreen(
    navigator: TermboxContract.Navigator,
    termboxViewModel: TermboxContract.TermboxViewModel = hiltViewModel<TermboxViewModel>()
) {
    var editState by remember { mutableStateOf(false) }

    if (editState) {
        TermboxEditView(
            onReadMode = { editState = false },
            viewModel = termboxViewModel
        )
    } else {
        TermboxView(
            onEditMode = { editState = true },
            viewModel = termboxViewModel,
            navigator = navigator
        )
    }
}
