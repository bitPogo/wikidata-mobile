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
import org.junit.Rule
import org.junit.Test
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.util.test.mustBe
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
                    onSearch = { },
                    onNew = { },
                    onRefresh = { },
                    onEdit = { },
                    onLanguageSearch = { },
                    onRandomEntity = { }
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
            .onNodeWithContentDescription("Refresh current entity")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription("Edit current entity")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription("Show more actions")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Add an entity")
            .assertDoesNotExist()

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
                    onNew = { },
                    onRefresh = { },
                    onEdit = { },
                    onLanguageSearch = { },
                    onRandomEntity = { }
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Search for another entity")
            .performClick()

        // Then
        wasCalled mustBe true
    }

    @Test
    fun Given_refresh_is_clicked_it_calls_the_delegated_function() {
        // Given
        var wasCalled = false
        val onRefresh = { wasCalled = true }

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermMenu(
                    title = fixture.fixture(),
                    isEditable = true,
                    onSearch = { },
                    onNew = { },
                    onRefresh = onRefresh,
                    onEdit = { },
                    onLanguageSearch = { },
                    onRandomEntity = { }
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Refresh current entity")
            .performClick()

        // Then
        wasCalled mustBe true
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
                    onSearch = { },
                    onNew = { },
                    onRefresh = { },
                    onEdit = onEdit,
                    onLanguageSearch = { },
                    onRandomEntity = { }
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Edit current entity")
            .performClick()

        // Then
        wasCalled mustBe true
    }

    @Test
    fun Given_show_more_is_clicked_it_shows_additional_items() {
        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermMenu(
                    title = fixture.fixture(),
                    isEditable = true,
                    onRefresh = { },
                    onSearch = { },
                    onNew = { },
                    onEdit = { },
                    onLanguageSearch = { },
                    onRandomEntity = { }
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Show more actions")
            .performClick()

        // Then
        composeTestRule
            .onNodeWithText("Add an entity")
            .assertIsDisplayed()

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
                    onSearch = { },
                    onNew = { },
                    onRefresh = { },
                    onEdit = { },
                    onLanguageSearch = { },
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
            .onNodeWithText("Add an entity")
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText("Select a random entity")
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText("Select another language")
            .assertDoesNotExist()

        wasCalled mustBe true
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
                    onSearch = { },
                    onNew = { },
                    onRefresh = { },
                    onEdit = { },
                    onLanguageSearch = onLanguageSearch,
                    onRandomEntity = { }
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
            .onNodeWithText("Add an entity")
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText("Select a random entity")
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText("Select another language")
            .assertDoesNotExist()

        wasCalled mustBe true
    }

    @Test
    fun Given_show_more_and_add_entity_is_clicked_it_delegates_the_call_to_the_given_function_while_closing_the_addional_item() {
        // Given
        var wasCalled = false
        val onAddEntity = { wasCalled = true }

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermMenu(
                    title = fixture.fixture(),
                    isEditable = true,
                    onSearch = { },
                    onNew = onAddEntity,
                    onRefresh = { },
                    onEdit = { },
                    onLanguageSearch = { },
                    onRandomEntity = { }
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Show more actions")
            .performClick()

        composeTestRule
            .onNodeWithText("Add an entity")
            .performClick()

        // Then
        composeTestRule
            .onNodeWithText("Add an entity")
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText("Select a random entity")
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText("Select another language")
            .assertDoesNotExist()

        wasCalled mustBe true
    }
}
