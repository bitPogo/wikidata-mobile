/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun String?.useResourceOnNullOrBlank(id: Int): String {
    return if (this.isNullOrBlank()) {
        stringResource(id)
    } else {
        this
    }
}
