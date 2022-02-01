/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.navigation

import androidx.navigation.NavController
import tech.antibytes.wikidata.app.navigation.NavigationContract.Routes

class Navigator(
    private val router: NavController
) : NavigationContract.Navigator {
    override fun goToTermbox() {
        router.navigate(Routes.TERMBOX.name) {
            launchSingleTop = true
        }
    }

    override fun goToLanguageSelector() = router.navigate(Routes.LANGUAGE_SELECTION.name)

    override fun goToTermSearch() = router.navigate(Routes.TERMSEARCH.name)
}
