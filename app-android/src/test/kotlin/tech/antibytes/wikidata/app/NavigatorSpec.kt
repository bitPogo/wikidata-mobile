/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import tech.antibytes.util.test.fulfils
import tech.antibytes.wikidata.app.navigation.NavigationContract
import tech.antibytes.wikidata.app.navigation.NavigationContract.Routes
import tech.antibytes.wikidata.app.navigation.Navigator

class NavigatorSpec {
    @Test
    fun `It fulfils Navigator`() {
        Navigator(mockk()) fulfils NavigationContract.Navigator::class
    }

    @Test
    fun `Given goToTermbox is called, it navigates to Termbox`() {
        // Given
        val navController: NavController = mockk()
        val builder: NavOptionsBuilder = mockk(relaxed = true)
        val capturedBuilder = slot<(NavOptionsBuilder) -> Unit>()

        every {
            navController.navigate(
                route = any(),
                builder = capture(capturedBuilder)
            )
        } just Runs

        // When
        Navigator(navController).goToTermbox()

        capturedBuilder.captured.invoke(builder)

        verify(exactly = 1) {
            navController.navigate(
                route = Routes.TERMBOX.name,
                builder = any()
            )
        }

        verify(exactly = 1) { builder.launchSingleTop = true }
    }

    @Test
    fun `Given goToLanguageSelector is called, it navigates to LanguageSelector`() {
        // Given
        val navController: NavController = mockk()

        every { navController.navigate(any<String>()) } just Runs

        // When
        Navigator(navController).goToLanguageSelector()

        verify(exactly = 1) { navController.navigate(Routes.LANGUAGE_SELECTION.name) }
    }

    @Test
    fun `Given goToTermSearch is called, it navigates to TermSearch`() {
        // Given
        val navController: NavController = mockk()

        every { navController.navigate(any<String>()) } just Runs

        // When
        Navigator(navController).goToTermSearch()

        verify(exactly = 1) { navController.navigate(Routes.TERMSEARCH.name) }
    }
}
