/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.mock

import tech.antibytes.wikidata.app.termbox.TermboxContract

class TermboxNavigatorStub(
    var goToLanguageSelector: (() -> Unit)? = null,
    var goToTermSearch: (() -> Unit)? = null
) : TermboxContract.Navigator {
    override fun goToLanguageSelector() {
        return goToLanguageSelector?.invoke()
            ?: throw RuntimeException("Missing Sideeffect goToLanguageSelector")
    }

    override fun goToTermSearch() {
        return goToTermSearch?.invoke()
            ?: throw RuntimeException("Missing Sideeffect goToTermSearch")
    }

    fun clear() {
        goToLanguageSelector = null
        goToTermSearch = null
    }
}
