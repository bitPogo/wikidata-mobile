/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.languageselector

import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

interface LanguageSelectorContract {
    interface LanguageSelectorViewModel {
        val currentLanguage: StateFlow<Locale>
        val selection: StateFlow<List<Locale>>
        val filter: StateFlow<String>

        fun setFilter(newFilter: String)
        fun selectLanguage(selector: Int)
    }

    fun interface Navigator {
        fun goToTermbox()
    }
}
