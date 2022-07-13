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
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.util.test.mustBe
import tech.antibytes.wikidata.app.ui.theme.WikidataMobileTheme

class AliasEditFieldSpec {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val fixture = kotlinFixture()

    @Test
    fun It_renders_a_editable_TextField() {
        // Given
        val label: String = fixture.fixture()
        val value: String = fixture.fixture()

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                AliasEditField(
                    label = label,
                    value = value,
                    onChange = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText(label)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(value)
            .assertIsDisplayed()
    }

    @Test
    fun It_contains_a_editable_TextField_without_a_value() {
        // Given
        val label: String = fixture.fixture()

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                AliasEditField(
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
                AliasEditField(
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
        capturedValue mustBe input
    }
}
