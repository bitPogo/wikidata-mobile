/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termbox

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.wikidata.app.ui.theme.WikidataMobileTheme

class TermboxEditMenuSpec {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun It_renders_a_TermboxEditMenu() {
        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermboxEditMenu(
                    onCancel = {},
                    onSave = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithContentDescription("Discard changes")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription("Save changes")
            .assertIsDisplayed()
    }

    @Test
    fun Given_cancel_is_clicked_it_calls_the_delegated_function() {
        // Given
        var wasCalled = false
        val onCancel = { wasCalled = true }

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermboxEditMenu(
                    onCancel = onCancel,
                    onSave = {}
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Discard changes")
            .performClick()

        // Then
        assertTrue(wasCalled)
    }

    @Test
    fun Given_save_is_clicked_it_shows_additional_items() {
        // Given
        var wasCalled = false
        val onSave = { wasCalled = true }

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermboxEditMenu(
                    onCancel = { },
                    onSave = onSave
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Save changes")
            .performClick()

        // Then
        assertTrue(wasCalled)
    }
}
