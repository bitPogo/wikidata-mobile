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
import androidx.compose.ui.test.performTextReplacement
import org.junit.Rule
import org.junit.Test
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kfixture.listFixture
import tech.antibytes.util.test.mustBe
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
                    onAliasInput = { _, _ -> Unit },
                    onNewAliasInput = {}
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
    fun Given_a_User_changes_the_Label_it_delegates_the_call_to_given_Lambda() {
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
                    onAliasInput = { _, _ -> Unit },
                    onNewAliasInput = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText(label)
            .performTextReplacement(newLabel)

        // Then
        newValue mustBe newLabel
    }

    @Test
    fun Given_a_User_changes_the_Description_it_delegates_the_call_to_given_Lambda() {
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
                    onAliasInput = { _, _ -> Unit },
                    onNewAliasInput = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText(description)
            .performTextReplacement(newDescription)

        // Then
        newValue mustBe newDescription
    }

    @Test
    fun Given_a_User_changes_an_Alias_it_delegates_the_call_to_given_Lambda() {
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
                    onAliasInput = onAliasChange,
                    onNewAliasInput = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText(aliases[3])
            .performTextReplacement(newAlias)

        // Then
        newValue mustBe newAlias
        capturedIndex mustBe 3
    }

    @Test
    fun Given_a_User_clicks_a_new_Alias_button_it_delegates_the_call_to_given_Lambda() {
        // Given
        var newValue: String? = null
        val onNewAlias = { givenValue: String ->
            newValue = givenValue
        }
        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermEditor(
                    label = fixture.fixture(),
                    description = fixture.fixture(),
                    aliases = fixture.listFixture(),
                    onLabelInput = { },
                    onDescriptionInput = { },
                    onAliasInput = { _, _ -> Unit },
                    onNewAliasInput = onNewAlias
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Add a new alias")
            .performClick()

        // Then
        newValue mustBe ""
    }
}
