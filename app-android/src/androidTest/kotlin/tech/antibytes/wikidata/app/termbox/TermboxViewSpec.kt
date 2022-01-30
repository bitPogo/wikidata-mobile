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
import tech.antibytes.wikidata.mock.TermBoxViewModelStub
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

    private val viewModel = TermBoxViewModelStub(
        id,
        editability,
        label,
        description,
        aliases,
        currentLanguage
    )

    @Before
    fun setUp() {
        viewModel.clear()

        id.update { "" }
        editability.update { true }
        label.update { "Test" }
        description.update { "" }
        aliases.update { emptyList() }
    }

    @Test
    fun It_renders_a_Termbox() {
        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermboxView(
                    onEditMode = {},
                    viewModel = viewModel
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithContentDescription("Edit the current entity")
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
                    viewModel = viewModel
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
                    viewModel = viewModel
                )
            }
        }

        this.editability.update { editability }

        // Then
        composeTestRule
            .onNodeWithContentDescription("Edit the current entity")
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
                    viewModel = viewModel
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
                    viewModel = viewModel
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
                    viewModel = viewModel
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
                    viewModel = viewModel
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
                    viewModel = viewModel
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
    fun Given_a_User_click_on_random_entity_it_delegates_the_call_to_the_ViewModel() {
        // Given
        var wasClicked = false
        viewModel.randomItem = { wasClicked = true }

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermboxView(
                    onEditMode = {},
                    viewModel = viewModel
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
    fun Given_a_User_click_on_edit_entity_it_delegates_the_call_to_given_function() {
        // Given
        var wasClicked = false
        val onEdit = { wasClicked = true }

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermboxView(
                    onEditMode = onEdit,
                    viewModel = viewModel
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Edit the current entity")
            .performClick()

        // Then
        assertTrue(wasClicked)
    }
}
