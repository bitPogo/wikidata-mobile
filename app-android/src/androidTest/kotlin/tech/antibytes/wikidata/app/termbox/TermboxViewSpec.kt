/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termbox

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fixture.listFixture
import tech.antibytes.wikidata.app.ui.theme.WikidataMobileTheme
import tech.antibytes.wikidata.mock.TermboxNavigatorStub
import tech.antibytes.wikidata.mock.TermboxViewModelStub
import java.util.Locale

class TermboxViewSpec {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val fixture = kotlinFixture()

    private val id = MutableStateFlow("")
    private val editability = MutableStateFlow(true)
    private val label = MutableStateFlow("Test")
    private val description = MutableStateFlow("")
    private val aliases = MutableStateFlow(emptyList<String>())

    private val currentLanguage = MutableStateFlow(Locale.ENGLISH)

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
        navigator.clear()

        id.update { "" }
        editability.update { true }
        label.update { "Test" }
        description.update { "" }
        aliases.update { emptyList() }
    }

    @Test
    fun It_renders_a_TermboxView() {
        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermboxView(
                    onEditMode = {},
                    viewModel = viewModel,
                    navigator = navigator
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithContentDescription("Edit current entity")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Test")
            .assertIsDisplayed()
    }

    @Test
    fun It_propagates_Id_changes_of_the_ViewModel() {
        // Given
        val id: String = fixture.fixture()

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermboxView(
                    onEditMode = {},
                    viewModel = viewModel,
                    navigator = navigator
                )
            }
        }

        this.id.update { id }

        // Then
        composeTestRule
            .onNodeWithText(id)
            .assertIsDisplayed()
    }

    @Test
    fun It_propagates_Editability_changes_of_the_ViewModel() {
        // Given
        val editability = false

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermboxView(
                    onEditMode = {},
                    viewModel = viewModel,
                    navigator = navigator
                )
            }
        }

        this.editability.update { editability }

        // Then
        composeTestRule
            .onNodeWithContentDescription("Edit current entity")
            .assertIsNotEnabled()
    }

    @Test
    fun It_propagates_Label_changes_of_the_ViewModel() {
        // Given
        val label: String = fixture.fixture()

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermboxView(
                    onEditMode = {},
                    viewModel = viewModel,
                    navigator = navigator
                )
            }
        }

        this.label.update { label }

        // Then
        composeTestRule
            .onNodeWithText(label)
            .assertIsDisplayed()
    }

    @Test
    fun Given_a_Label_is_empty_it_uses_a_resource_string() {
        // Given
        val label = ""

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermboxView(
                    onEditMode = {},
                    viewModel = viewModel,
                    navigator = navigator
                )
            }
        }

        this.label.update { label }

        // Then
        composeTestRule
            .onNodeWithText("No label defined")
            .assertIsDisplayed()
    }

    @Test
    fun It_propagates_Description_changes_of_the_ViewModel() {
        // Given
        val description: String = fixture.fixture()

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermboxView(
                    onEditMode = {},
                    viewModel = viewModel,
                    navigator = navigator
                )
            }
        }

        this.description.update { description }

        // Then
        composeTestRule
            .onNodeWithText(description)
            .assertIsDisplayed()
    }

    @Test
    fun Given_a_Description_is_empty_it_uses_a_resource_string() {
        // Given
        val description = ""

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermboxView(
                    onEditMode = {},
                    viewModel = viewModel,
                    navigator = navigator
                )
            }
        }

        this.description.update { description }

        // Then
        composeTestRule
            .onNodeWithText("No description defined")
            .assertIsDisplayed()
    }

    @Test
    fun It_propagates_Alias_changes_of_the_ViewModel() {
        // Given
        val aliases: List<String> = fixture.listFixture(size = 5)

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermboxView(
                    onEditMode = {},
                    viewModel = viewModel,
                    navigator = navigator
                )
            }
        }

        this.aliases.update { aliases }

        // Then
        composeTestRule
            .onNodeWithText(aliases.first())
            .assertIsDisplayed()
    }

    @Test
    fun Given_a_User_clicks_on_random_entity_it_delegates_the_call_to_the_ViewModel() {
        // Given
        var wasClicked = false
        viewModel.randomItem = { wasClicked = true }

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermboxView(
                    onEditMode = {},
                    viewModel = viewModel,
                    navigator = navigator
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Show more actions")
            .performClick()

        composeTestRule
            .onNodeWithText("Select a random entity")
            .performClick()

        // Then
        assertTrue(wasClicked)
    }

    @Test
    fun Given_a_User_clicks_on_edit_entity_it_delegates_the_call_to_given_function() {
        // Given
        var wasClicked = false
        val onEdit = { wasClicked = true }

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermboxView(
                    onEditMode = onEdit,
                    viewModel = viewModel,
                    navigator = navigator
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Edit current entity")
            .performClick()

        // Then
        assertTrue(wasClicked)
    }

    @Test
    fun Given_a_User_clicks_on_refresh_entity_it_delegates_the_call_to_given_function() {
        // Given
        var wasClicked = false
        viewModel.refresh = { wasClicked = true }

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermboxView(
                    onEditMode = {},
                    viewModel = viewModel,
                    navigator = navigator
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Refresh current entity")
            .performClick()

        // Then
        assertTrue(wasClicked)
    }

    @Test
    fun Given_a_User_clicks_on_search_button_it_delegates_the_call_to_the_Navigator() {
        // Given
        var wasClicked = false
        navigator.goToTermSearch = { wasClicked = true }

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermboxView(
                    onEditMode = {},
                    viewModel = viewModel,
                    navigator = navigator
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Search for another entity")
            .performClick()

        // Then
        assertTrue(wasClicked)
    }

    @Test
    fun Given_a_User_clicks_on_language_selection_it_delegates_the_call_to_the_Navigator() {
        // Given
        var wasClicked = false
        navigator.goToLanguageSelector = { wasClicked = true }

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermboxView(
                    onEditMode = {},
                    viewModel = viewModel,
                    navigator = navigator
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Show more actions")
            .performClick()

        composeTestRule
            .onNodeWithText("Select another language")
            .performClick()

        // Then
        assertTrue(wasClicked)
    }
}
