/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termbox

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fixture.listFixture
import tech.antibytes.wikidata.app.ui.theme.WikidataMobileTheme

class TermViewSpec {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val fixture = kotlinFixture()

    @Test
    fun It_renders_a_TermView() {
        // Given
        val label: String = fixture.fixture()
        val description: String = fixture.fixture()

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermView(
                    label = label,
                    description = description,
                    aliases = emptyList()
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText(label)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(description)
            .assertIsDisplayed()
    }

    @Test
    fun It_renders_a_TermView_without_Aliases_if_there_a_empty_List_of_Aliases_is_given() {
        // Given
        val aliases = emptyList<String>()

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermView(
                    label = fixture.fixture(),
                    description = fixture.fixture(),
                    aliases = aliases
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("Also known as:")
            .assertDoesNotExist()
    }

    @Test
    fun It_renders_a_TermView_without_Aliases_if_there_a_List_of_Aliases_is_given() {
        // Given
        val aliases = fixture.listFixture<String>()

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermView(
                    label = fixture.fixture(),
                    description = fixture.fixture(),
                    aliases = aliases
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("Also known as:")
            .assertIsDisplayed()

        aliases.forEach { alias ->
            composeTestRule
                .onNodeWithText(alias)
                .assertIsDisplayed()
        }
    }
}
