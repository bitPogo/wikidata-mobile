/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.languageselector

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import tech.antibytes.wikidata.app.ui.theme.WikidataMobileTheme
import java.util.Locale

class LanguageItemSpec {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun It_renders_a_LanguageItem() {
        // Given
        val value = Locale.GERMAN

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                LanguageItem(
                    23,
                    value = value,
                    selected = Locale.CANADA,
                    {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText(value.displayName)
            .assertIsDisplayed()
    }

    @Test
    fun Given_the_item_is_clicked_it_delegates_the_call_and_its_value_to_the_given_lambda() {
        // Given
        val id = 23
        val value = Locale.GERMAN

        var capturedId: Int? = null
        val onClick = { givenId: Int ->
            capturedId = givenId
        }
        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                LanguageItem(
                    id,
                    value = value,
                    selected = Locale.CANADA,
                    onClick
                )
            }
        }

        composeTestRule
            .onNodeWithText(value.displayName)
            .performClick()

        // Then
        assertEquals(
            id,
            capturedId
        )
    }
}
