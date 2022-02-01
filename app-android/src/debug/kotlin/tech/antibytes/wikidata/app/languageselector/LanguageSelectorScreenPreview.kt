/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.languageselector

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale
import java.util.Locale.CHINESE
import java.util.Locale.ENGLISH
import java.util.Locale.GERMAN
import java.util.Locale.KOREAN

@Preview
@Composable
fun LanguageScreenPreview() {
    LanguageSelectorScreen(languageSelectorViewModel = LanguageScreenViewModelStub())
}

private class LanguageScreenViewModelStub : LanguageSelectorContract.LanguageSelectorViewModel {
    override val currentLanguage: StateFlow<Locale> = MutableStateFlow(ENGLISH)
    override val selection: StateFlow<List<Locale>> = MutableStateFlow(
        listOf(
            ENGLISH,
            GERMAN,
            CHINESE,
            KOREAN,
        )
    )
    override val filter: StateFlow<String> = MutableStateFlow("")

    override fun setFilter(newFilter: String) = Unit

    override fun selectLanguage(selector: Int) = Unit
}
