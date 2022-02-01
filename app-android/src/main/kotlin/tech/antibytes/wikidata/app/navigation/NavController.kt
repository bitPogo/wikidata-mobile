/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.MutableStateFlow
import tech.antibytes.wikidata.app.navigation.NavigationContract.Routes
import tech.antibytes.wikidata.app.languageselector.LanguageSelectorScreen
import tech.antibytes.wikidata.app.login.LoginScreen
import tech.antibytes.wikidata.app.termbox.TermboxScreen
import tech.antibytes.wikidata.app.termsearch.TermSearchScreen

@Composable
fun Routing() {
    val controller = rememberNavController()
    
    NavHost(navController = controller, startDestination = Routes.LOGIN_SCREEN.name) {
        composable(route = Routes.LOGIN_SCREEN.name) {
            LoginScreen()
        }

        composable(route = Routes.TERMBOX.name) {
            // TermboxScreen()
        }

        composable(route = Routes.TERMSEARCH.name) {
            TermSearchScreen()
        }

        composable(route = Routes.LANGUAGE_SELECTION.name) {
            LanguageSelectorScreen()
        }
    }
}
