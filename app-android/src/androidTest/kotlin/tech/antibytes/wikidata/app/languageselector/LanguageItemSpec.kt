/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.languageselector

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.mustBe
import tech.antibytes.wikidata.app.ui.theme.WikidataMobileTheme
import tech.antibytes.wikidata.mock.MwLocaleAndroidStub
import java.util.Locale.ENGLISH
import java.util.Locale.GERMAN
import java.util.Locale.KOREAN

class LanguageItemSpec {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val fixture = kotlinFixture()

    @Test
    fun It_renders_a_LanguageItem() {
        // Given
        val value = MwLocaleAndroidStub(
            fixture.fixture(),
            fixture.fixture(),
            ENGLISH
        )

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                LanguageItem(
                    23,
                    value = value,
                    selected = MwLocaleAndroidStub(
                        fixture.fixture(),
                        fixture.fixture(),
                        GERMAN
                    ),
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
    fun It_selects_items() {
        // Given
        val id = 23
        val value = MwLocaleAndroidStub(
            fixture.fixture(),
            fixture.fixture(),
            ENGLISH
        )

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                LanguageItem(
                    id,
                    value = value,
                    selected = value,
                    { }
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText(value.displayName)
            .onChildAt(0)
            .assertIsSelected()
    }

    @Test
    fun Given_the_item_is_clicked_it_delegates_the_call_and_its_value_to_the_given_lambda() {
        // Given
        val id = 23
        val value = MwLocaleAndroidStub(
            fixture.fixture(),
            fixture.fixture(),
            KOREAN
        )

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
                    selected = MwLocaleAndroidStub(
                        fixture.fixture(),
                        fixture.fixture(),
                        KOREAN
                    ),
                    onClick
                )
            }
        }

        composeTestRule
            .onNodeWithText(value.displayName)
            .performClick()

        // Then
        capturedId mustBe id
    }
}
