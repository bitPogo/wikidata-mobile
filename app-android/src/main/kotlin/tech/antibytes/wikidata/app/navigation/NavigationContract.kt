/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.navigation

import tech.antibytes.wikidata.app.languageselector.LanguageSelectorContract
import tech.antibytes.wikidata.app.login.LoginContract
import tech.antibytes.wikidata.app.termbox.TermboxContract
import tech.antibytes.wikidata.app.termsearch.TermSearchContract

interface NavigationContract {
    interface Navigator :
        LanguageSelectorContract.Navigator,
        LoginContract.Navigator,
        TermboxContract.Navigator,
        TermSearchContract.Navigator

    enum class Routes {
        LOGIN_SCREEN,
        TERMBOX,
        TERMSEARCH,
        LANGUAGE_SELECTION
    }
}
