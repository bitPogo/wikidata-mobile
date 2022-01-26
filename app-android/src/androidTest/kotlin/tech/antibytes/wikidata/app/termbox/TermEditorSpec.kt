/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termbox

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextReplacement
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fixture.listFixture
import tech.antibytes.wikidata.app.ui.theme.WikidataMobileTheme

class TermEditorSpec {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val fixture = kotlinFixture()

    @Test
    fun It_renders_a_TermEditor() {
        // Given
        val label: String = fixture.fixture()
        val description: String = fixture.fixture()
        val aliases: List<String> = fixture.listFixture(size = 5)

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermEditor(
                    label = label,
                    description = description,
                    aliases = aliases,
                    onLabelInput = {},
                    onDescriptionInput = {},
                    onAliasInput = {_, _ -> Unit}
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

        aliases.forEach { alias ->
            composeTestRule
                .onNodeWithText(alias)
                .assertIsDisplayed()
        }
    }

    @Test
    fun It_delegates_the_value_change_of_the_label() {
        // Given
        val label: String = fixture.fixture()
        val newLabel: String = fixture.fixture()

        var newValue: String? = null
        val onLabelChange = { givenValue: String ->
            newValue = givenValue
        }
        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermEditor(
                    label = label,
                    description = fixture.fixture(),
                    aliases = emptyList(),
                    onLabelInput = onLabelChange,
                    onDescriptionInput = {},
                    onAliasInput = {_, _ -> Unit}
                )
            }
        }

        composeTestRule
            .onNodeWithText(label)
            .performTextReplacement(newLabel)

        // Then
        assertEquals(
            newLabel,
            newValue,
        )
    }

    @Test
    fun It_delegates_the_value_change_of_the_Description() {
        // Given
        val description: String = fixture.fixture()
        val newDescription: String = fixture.fixture()

        var newValue: String? = null
        val onDescriptionChange = { givenValue: String ->
            newValue = givenValue
        }
        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermEditor(
                    label = fixture.fixture(),
                    description = description,
                    aliases = emptyList(),
                    onLabelInput = { },
                    onDescriptionInput = onDescriptionChange,
                    onAliasInput = {_, _ -> Unit}
                )
            }
        }


        composeTestRule
            .onNodeWithText(description)
            .performTextReplacement(newDescription)

        // Then
        assertEquals(
            newDescription,
            newValue,
        )
    }

    @Test
    fun It_delegates_the_value_change_of_an_Alias() {
        // Given
        val aliases: List<String> = fixture.listFixture(size = 5)
        val newAlias: String = fixture.fixture()

        var capturedIndex: Int? = null
        var newValue: String? = null
        val onAliasChange = { givenIndex: Int, givenValue: String ->
            capturedIndex = givenIndex
            newValue = givenValue
        }
        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermEditor(
                    label = fixture.fixture(),
                    description = fixture.fixture(),
                    aliases = aliases,
                    onLabelInput = { },
                    onDescriptionInput = { },
                    onAliasInput = onAliasChange
                )
            }
        }


        composeTestRule
            .onNodeWithText(aliases[3])
            .performTextReplacement(newAlias)

        // Then
        assertEquals(
            newAlias,
            newValue,
        )

        assertEquals(
            3,
            capturedIndex,
        )
    }
}
