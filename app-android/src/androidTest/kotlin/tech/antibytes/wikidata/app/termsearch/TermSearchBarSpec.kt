/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termsearch

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.wikidata.app.ui.theme.WikidataMobileTheme

class TermSearchBarSpec {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val fixture = kotlinFixture()

    @Test
    fun It_renders_a_TermSearchBar() {
        // Given
        val value: String = fixture.fixture()

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermSearchBar(
                    value = value,
                    onValueChange = {},
                    onSearch = {},
                    onCancel = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText(value)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription("Cancel search")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription("Apply your query")
            .assertIsDisplayed()
    }

    @Test
    fun It_delegates_value_changes_to_the_given_lambda() {
        // Given
        val oldValue: String = fixture.fixture()
        val newValue: String = fixture.fixture()

        var capturedValue: String? = null
        val onValueChange = { givenValue: String ->
            capturedValue = givenValue
        }
        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermSearchBar(
                    value = oldValue,
                    onValueChange = onValueChange,
                    onSearch = {},
                    onCancel = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText(oldValue)
            .performTextReplacement(newValue)

        // Then
        assertEquals(
            newValue,
            capturedValue
        )
    }

    @Test
    fun Given_the_search_button_is_clicked_it_calls_the_given_lambda() {
        // Given
        var wasCalled = false
        val onSearch = {
            wasCalled = true
        }
        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermSearchBar(
                    value = fixture.fixture(),
                    onValueChange = {},
                    onSearch = onSearch,
                    onCancel = {}
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Apply your query")
            .performClick()

        // Then
        assertTrue(wasCalled)
    }

    @Test
    fun Given_the_cancel_button_is_clicked_it_calls_the_given_lambda() {
        // Given
        var wasCalled = false
        val onCancel = {
            wasCalled = true
        }

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermSearchBar(
                    value = fixture.fixture(),
                    onValueChange = {},
                    onSearch = {},
                    onCancel = onCancel
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Cancel search")
            .performClick()

        // Then
        assertTrue(wasCalled)
    }
}
