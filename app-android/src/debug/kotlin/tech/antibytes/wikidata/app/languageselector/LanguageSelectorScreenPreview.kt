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
import tech.antibytes.wikidata.app.util.MwLocale
import tech.antibytes.wikidata.app.util.UtilContract
import java.util.Locale.KOREAN

@Preview
@Composable
fun LanguageScreenPreview() {
    LanguageSelectorScreen(languageSelectorViewModel = LanguageScreenViewModelStub())
}

private class LanguageScreenViewModelStub : LanguageSelectorContract.LanguageSelectorViewModel {
    override val currentLanguage: StateFlow<UtilContract.MwLocale> = MutableStateFlow(MwLocale("de"))
    override val selection: StateFlow<List<UtilContract.MwLocale>> = MutableStateFlow(
        listOf(
            MwLocale("en"),
            MwLocale("de"),
            MwLocale("zh"),
            MwLocale(KOREAN.toLanguageTag()),
        )
    )
    override val filter: StateFlow<String> = MutableStateFlow("")

    override fun setFilter(newFilter: String) = Unit

    override fun selectLanguage(selector: Int) = Unit
}
