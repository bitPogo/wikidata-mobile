/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.languageselector

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextReplacement
import org.junit.Rule
import org.junit.Test
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.util.test.mustBe
import tech.antibytes.wikidata.app.ui.theme.WikidataMobileTheme

class LanguageSearchBarSpec {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val fixture = kotlinFixture()

    @Test
    fun It_renders_a_LanguageSearchBar() {
        // Given
        val value: String = fixture.fixture()

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                LanguageSearchBar(
                    value = value,
                    onValueChange = {},
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText(value)
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
                LanguageSearchBar(
                    value = oldValue,
                    onValueChange = onValueChange,
                )
            }
        }

        composeTestRule
            .onNodeWithText(oldValue)
            .performTextReplacement(newValue)

        // Then
        capturedValue mustBe newValue
    }
}
