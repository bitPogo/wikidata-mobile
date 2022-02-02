/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.languageselector

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import tech.antibytes.wikidata.app.util.MwLocale

@Preview
@Composable
fun LanguageItemPreviewSelected() {
    LanguageItem(
        id = 23,
        value = MwLocale("de-DE"),
        selected = MwLocale("de-DE"),
        {}
    )
}

@Preview
@Composable
fun LanguageItemPreviewUnSelected() {
    LanguageItem(
        id = 42,
        value = MwLocale("en"),
        selected = MwLocale("de-DE"),
        {}
    )
}
