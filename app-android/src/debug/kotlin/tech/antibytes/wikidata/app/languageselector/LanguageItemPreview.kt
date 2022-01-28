/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.languageselector

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import java.util.Locale

@Preview
@Composable
fun LanguageItemPreviewSelected() {
    LanguageItem(
        id = 23,
        value = Locale.GERMANY,
        selected = Locale.GERMANY,
        {}
    )
}

@Preview
@Composable
fun LanguageItemPreviewUnSelected() {
    LanguageItem(
        id = 42,
        value = Locale.ENGLISH,
        selected = Locale.GERMANY,
        {}
    )
}
