/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.ui.atom

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.wikidata.app.ui.theme.WikidataMobileTheme

class PasswordFieldSpec {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val fixture = kotlinFixture()

    @Test
    fun It_contains_a_editable_TextField_without_a_value() {
        // Given
        val label: String = fixture.fixture()

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                PasswordField(
                    label = label,
                    value = "",
                    onChange = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText(label)
            .assertIsDisplayed()
    }

    @Test
    fun It_contains_a_editable_TextField_with_protected_value() {
        // Given
        val label: String = fixture.fixture()
        val value: String = fixture.fixture()

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                PasswordField(
                    label = label,
                    value = value,
                    onChange = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText(value)
            .assertDoesNotExist()
    }

    @Test
    fun It_contains_a_editable_TextField_with_unprotected_value_if_show_button_was_clicked() {
        // Given
        val label: String = fixture.fixture()
        val value: String = fixture.fixture()

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                PasswordField(
                    label = label,
                    value = value,
                    onChange = {}
                )
            }
        }

        composeTestRule
            .onRoot()
            .onChildAt(0)
            .onChildAt(0)
            .performClick()

        // Then
        composeTestRule
            .onNodeWithText(value)
            .assertExists()
    }

    @Test
    fun It_contains_a_editable_TextField_with_protected_value_if_show_button_was_doubled_clicked() {
        // Given
        val label: String = fixture.fixture()
        val value: String = fixture.fixture()

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                PasswordField(
                    label = label,
                    value = value,
                    onChange = {}
                )
            }
        }

        composeTestRule
            .onRoot()
            .onChildAt(0)
            .onChildAt(0)
            .performClick()
            .performClick()

        // Then
        composeTestRule
            .onNodeWithText(value)
            .assertDoesNotExist()
    }

    @Test
    fun It_delegates_a_new_value_to_the_given_onChange_lambda() {
        // Given
        val input: String = fixture.fixture()

        var capturedValue: String? = null
        val onChange = { givenValue: String ->
            capturedValue = givenValue
        }
        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                PasswordField(
                    label = fixture.fixture(),
                    value = "",
                    onChange = onChange
                )
            }
        }

        composeTestRule
            .onRoot()
            .onChildAt(0)
            .performTextInput(input)

        // Then
        assertEquals(
            capturedValue,
            input
        )
    }
}
