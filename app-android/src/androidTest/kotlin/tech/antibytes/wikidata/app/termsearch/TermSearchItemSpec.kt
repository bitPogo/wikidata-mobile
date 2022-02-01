/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termsearch

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.wikidata.app.ui.theme.WikidataMobileTheme

class TermSearchItemSpec {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val fixture = kotlinFixture()

    @Test
    fun It_renders_a_TermSearchItem() {
        // Given
        val label: String = fixture.fixture()
        val description: String = fixture.fixture()

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermSearchItem(
                    label = label,
                    description = description,
                    onClick = {}
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
    fun Given_an_Item_is_clicked_it_delegates_the_call_and_id_to_the_given_lambda() {
        // Given
        val label: String = fixture.fixture()

        var wasCalled = false
        val onClick = { wasCalled = true }

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermSearchItem(
                    label = label,
                    description = fixture.fixture(),
                    onClick = onClick
                )
            }
        }

        composeTestRule
            .onNodeWithText(label)
            .performClick()

        // Then
        assertTrue(wasCalled)
    }
}
