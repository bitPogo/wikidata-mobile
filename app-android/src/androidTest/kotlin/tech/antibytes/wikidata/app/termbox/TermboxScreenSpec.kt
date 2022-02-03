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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.wikidata.app.ui.theme.WikidataMobileTheme
import tech.antibytes.wikidata.mock.MwLocaleAndroidStub
import tech.antibytes.wikidata.mock.TermboxNavigatorStub
import tech.antibytes.wikidata.mock.TermboxViewModelStub
import java.util.Locale.ENGLISH

class TermboxScreenSpec {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val fixture = kotlinFixture()

    private val defaultLabel: String = fixture.fixture()

    private val id = MutableStateFlow("")
    private val editability = MutableStateFlow(true)
    private val label = MutableStateFlow(defaultLabel)
    private val description = MutableStateFlow("")
    private val aliases = MutableStateFlow(emptyList<String>())

    private val currentLanguage = MutableStateFlow(
        MwLocaleAndroidStub(fixture.fixture(), fixture.fixture(), ENGLISH)
    )

    private val viewModel = TermboxViewModelStub(
        id,
        editability,
        label,
        description,
        aliases,
        currentLanguage
    )

    private val navigator = TermboxNavigatorStub()

    @Before
    fun setUp() {
        viewModel.clear()

        id.update { "" }
        editability.update { true }
        label.update { defaultLabel }
        description.update { "" }
        aliases.update { emptyList() }
    }

    @Test
    fun It_renders_a_TermboxScreen() {
        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermboxScreen(
                    navigator = navigator,
                    termboxViewModel = viewModel
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithContentDescription("Edit current entity")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(defaultLabel)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription("Discard changes")
            .assertDoesNotExist()
    }

    @Test
    fun Given_a_User_clicks_on_the_Edit_button_it_switches_into_EditMode() {
        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermboxScreen(
                    navigator = navigator,
                    termboxViewModel = viewModel
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Edit current entity")
            .performClick()

        // Then
        composeTestRule
            .onNodeWithContentDescription("Discard changes")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(defaultLabel)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription("Edit current entity")
            .assertDoesNotExist()
    }

    @Test
    fun Given_a_User_clicks_on_the_Cancel_button_while_in_EditMode_it_switches_into_ReadMode() {
        // Given
        viewModel.dischargeChanges = {}

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermboxScreen(
                    navigator = navigator,
                    termboxViewModel = viewModel
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Edit current entity")
            .performClick()

        composeTestRule
            .onNodeWithContentDescription("Discard changes")
            .performClick()

        // Then
        composeTestRule
            .onNodeWithContentDescription("Edit current entity")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(defaultLabel)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription("Discard changes")
            .assertDoesNotExist()
    }
}
