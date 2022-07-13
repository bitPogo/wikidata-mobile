/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.ui.atom

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

class TopSearchBarSpec {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val fixture = kotlinFixture()

    @Test
    fun It_renders_a_TopSearchBar() {
        // Given
        val value: String = fixture.fixture()

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TopSearchBar(
                    value = value,
                    onValueChange = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText(value)
            .assertIsDisplayed()
    }

    @Test
    fun Given_the_value_was_changed_it_delegates_the_new_value_to_the_given_lambda() {
        // Given
        val oldValue: String = fixture.fixture()
        val newValue: String = fixture.fixture()

        var capturedValue: String? = null
        val onChange = { givenValue: String ->
            capturedValue = givenValue
        }
        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TopSearchBar(
                    value = oldValue,
                    onValueChange = onChange
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
