/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.extension

import androidx.compose.material.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.wikidata.app.R
import tech.antibytes.wikidata.app.ui.theme.WikidataMobileTheme

class StringExtensionSpec {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val fixture = kotlinFixture()

    @Test
    fun Givne_useResourceOnNullOrBlank_is_called_with_on_a_String_it_returns_its_value() {
        // Given
        val expected: String = fixture.fixture()

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                Text(text = expected.useResourceOnNullOrBlank(id = R.string.termbox_missing_label))
            }
        }

// Then
        composeTestRule
            .onNodeWithText(expected)
            .assertIsDisplayed()
    }

    @Test
    fun Givne_useResourceOnNullOrBlank_is_called_with_on_a_blank_String_it_returns_the_given_ResourceString() {
        // Given
        val given = "      "

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                Text(text = given.useResourceOnNullOrBlank(id = R.string.termbox_missing_label))
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("No label defined")
            .assertIsDisplayed()
    }

    @Test
    fun Givne_useResourceOnNullOrBlank_is_called_with_on_a_empty_String_it_returns_the_given_ResourceString() {
        // Given
        val given = ""

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                Text(text = given.useResourceOnNullOrBlank(id = R.string.termbox_missing_label))
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("No label defined")
            .assertIsDisplayed()
    }

    @Test
    fun Givne_useResourceOnNullOrBlank_is_called_with_on_a_null_String_it_returns_the_given_ResourceString() {
        // Given
        val given = null

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                Text(text = given.useResourceOnNullOrBlank(id = R.string.termbox_missing_label))
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("No label defined")
            .assertIsDisplayed()
    }
}
