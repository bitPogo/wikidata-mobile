/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import tech.antibytes.wikidata.app.languageselector.LanguageSelectorScreen
import tech.antibytes.wikidata.app.login.LoginScreen
import tech.antibytes.wikidata.app.navigation.NavigationContract.Routes
import tech.antibytes.wikidata.app.termbox.TermboxScreen
import tech.antibytes.wikidata.app.termsearch.TermSearchScreen

@Composable
fun Routing() {
    val controller = rememberNavController()
    val router by remember { mutableStateOf(Navigator(controller)) }

    NavHost(navController = controller, startDestination = Routes.LOGIN_SCREEN.name) {
        composable(route = Routes.LOGIN_SCREEN.name) {
            Log.d("ROUTING", "GOTO ${Routes.LOGIN_SCREEN.name}")
            LoginScreen(navigator = router)
        }

        composable(route = Routes.TERMBOX.name) {
            Log.d("ROUTING", "GOTO ${Routes.TERMBOX.name}")
            TermboxScreen(navigator = router)
        }

        composable(route = Routes.TERMSEARCH.name) {
            Log.d("ROUTING", "GOTO ${Routes.TERMSEARCH.name}")
            TermSearchScreen(navigator = router)
        }

        composable(route = Routes.LANGUAGE_SELECTION.name) {
            Log.d("ROUTING", "GOTO ${Routes.LANGUAGE_SELECTION.name}")
            LanguageSelectorScreen(navigator = router)
        }
    }
}
