/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.ui.atom

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.wikidata.app.ui.theme.WikidataMobileTheme

class SimpleButtonSpec {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val fixture = kotlinFixture()

    @Test
    fun It_renders_a_Button() {
        // Given
        val label: String = fixture.fixture()

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                SimpleButton(
                    label = label,
                    onClick = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText(label)
            .assertIsDisplayed()
    }

    @Test
    fun It_delegates_the_click_event_to_the_given_function() {
        // Given
        val label: String = fixture.fixture()

        var wasClicked = false
        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                SimpleButton(
                    label = label,
                    onClick = { wasClicked = true }
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText(label)
            .performClick()
            .performClick()

        assertTrue(wasClicked)
    }
}
