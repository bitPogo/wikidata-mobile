/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.extension

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import kotlinx.coroutines.yield

fun Modifier.focusItem() = composed {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(true) {
        yield()
        focusRequester.requestFocus()
    }

    focusRequester(
        focusRequester = focusRequester
    )
}
