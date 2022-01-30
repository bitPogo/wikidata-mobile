/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termbox

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.wikidata.app.ui.theme.WikidataMobileTheme

class TermboxMenuSpec {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val fixture = kotlinFixture()

    @Test
    fun It_renders_a_TopBar() {
        // Given
        val title: String = fixture.fixture()

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermMenu(
                    title = title,
                    isEditable = true,
                    onSearch = { /*TODO*/ },
                    onEdit = { /*TODO*/ },
                    onLanguageSearch = { /*TODO*/ },
                    onRandomEntity = { /*TODO*/ }
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText(title)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription("Search for another entity")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription("Edit current entity")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription("Show more actions")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Select a random entity")
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText("Select another language")
            .assertDoesNotExist()
    }

    @Test
    fun Given_search_is_clicked_it_calls_the_delegated_function() {
        // Given
        var wasCalled = false
        val onSearch = { wasCalled = true }

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermMenu(
                    title = fixture.fixture(),
                    isEditable = true,
                    onSearch = onSearch,
                    onEdit = { /*TODO*/ },
                    onLanguageSearch = { /*TODO*/ },
                    onRandomEntity = { /*TODO*/ }
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Search for another entity")
            .performClick()

        // Then
        assertTrue(wasCalled)
    }

    @Test
    fun Given_edit_is_clicked_it_calls_the_delegated_function() {
        // Given
        var wasCalled = false
        val onEdit = { wasCalled = true }

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermMenu(
                    title = fixture.fixture(),
                    isEditable = true,
                    onSearch = { /*TODO*/ },
                    onEdit = onEdit,
                    onLanguageSearch = { /*TODO*/ },
                    onRandomEntity = { /*TODO*/ }
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Edit current entity")
            .performClick()

        // Then
        assertTrue(wasCalled)
    }

    @Test
    fun Given_show_more_is_clicked_it_shows_additional_items() {
        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermMenu(
                    title = fixture.fixture(),
                    isEditable = true,
                    onSearch = { /*TODO*/ },
                    onEdit = { /*TODO*/ },
                    onLanguageSearch = { /*TODO*/ },
                    onRandomEntity = { /*TODO*/ }
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Show more actions")
            .performClick()

        // Then
        composeTestRule
            .onNodeWithText("Select a random entity")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Select another language")
            .assertIsDisplayed()
    }

    @Test
    fun Given_show_more_and_random_is_clicked_it_delegates_the_call_to_the_given_function_while_closing_the_addional_item() {
        // Given
        var wasCalled = false
        val onRandom = { wasCalled = true }

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermMenu(
                    title = fixture.fixture(),
                    isEditable = true,
                    onSearch = { /*TODO*/ },
                    onEdit = { /*TODO*/ },
                    onLanguageSearch = { /*TODO*/ },
                    onRandomEntity = onRandom
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Show more actions")
            .performClick()

        composeTestRule
            .onNodeWithText("Select a random entity")
            .performClick()

        // Then
        composeTestRule
            .onNodeWithText("Select a random entity")
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText("Select another language")
            .assertDoesNotExist()

        assertTrue(wasCalled)
    }

    @Test
    fun Given_show_more_and_language_search_is_clicked_it_delegates_the_call_to_the_given_function_while_closing_the_addional_item() {
        // Given
        var wasCalled = false
        val onLanguageSearch = { wasCalled = true }

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermMenu(
                    title = fixture.fixture(),
                    isEditable = true,
                    onSearch = { /*TODO*/ },
                    onEdit = { /*TODO*/ },
                    onLanguageSearch = onLanguageSearch,
                    onRandomEntity = { /*TODO*/ }
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Show more actions")
            .performClick()

        composeTestRule
            .onNodeWithText("Select another language")
            .performClick()

        // Then
        composeTestRule
            .onNodeWithText("Select a random entity")
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText("Select another language")
            .assertDoesNotExist()

        assertTrue(wasCalled)
    }
}
