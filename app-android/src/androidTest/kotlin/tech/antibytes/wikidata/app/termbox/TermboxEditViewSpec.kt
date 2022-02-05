/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termbox

import android.graphics.Bitmap
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fixture.listFixture
import tech.antibytes.util.test.mustBe
import tech.antibytes.wikidata.app.ui.theme.WikidataMobileTheme
import tech.antibytes.wikidata.mock.MwLocaleAndroidStub
import tech.antibytes.wikidata.mock.TermboxViewModelStub
import java.util.Locale.ENGLISH

class TermboxEditViewSpec {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val fixture = kotlinFixture()

    private val id = MutableStateFlow("")
    private val editability = MutableStateFlow(true)
    private val label = MutableStateFlow("")
    private val description = MutableStateFlow("")
    private val aliases = MutableStateFlow(emptyList<String>())
    private val qrCode = MutableStateFlow<Bitmap?>(null)

    private val currentLanguage = MutableStateFlow(
        MwLocaleAndroidStub(fixture.fixture(), fixture.fixture(), ENGLISH)
    )

    private val viewModel = TermboxViewModelStub(
        id,
        editability,
        label,
        description,
        aliases,
        currentLanguage,
        qrCode
    )

    @Before
    fun setUp() {
        viewModel.clear()

        id.update { "" }
        editability.update { true }
        label.update { "" }
        description.update { "" }
        aliases.update { emptyList() }
    }

    @Test
    fun It_renders_a_TermboxEditView() {
        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermboxEditView(
                    onReadMode = { },
                    viewModel = viewModel
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithContentDescription("Discard changes")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Label")
            .assertIsDisplayed()
    }

    @Test
    fun It_propagates_Label_changes_of_the_ViewModel() {
        // Given
        val label: String = fixture.fixture()

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermboxEditView(
                    onReadMode = { },
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
    fun Given_a_User_changes_the_Label_it_delegates_the_call_to_the_ViewModel() {
        // Given
        val label: String = fixture.fixture()

        var capturedLabel: String? = null
        viewModel.setLabel = { givenLabel ->
            capturedLabel = givenLabel
        }
        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermboxEditView(
                    onReadMode = { },
                    viewModel = viewModel
                )
            }
        }

        composeTestRule
            .onNodeWithText("Label")
            .performTextInput(label)

        // Then
        capturedLabel mustBe label
    }

    @Test
    fun It_propagates_Description_changes_of_the_ViewModel() {
        // Given
        val description: String = fixture.fixture()

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermboxEditView(
                    onReadMode = { },
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
    fun Given_a_User_changes_the_Description_it_delegates_the_call_to_the_ViewModel() {
        // Given
        val description: String = fixture.fixture()

        var capturedDescription: String? = null
        viewModel.setDescription = { givenDescription ->
            capturedDescription = givenDescription
        }
        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermboxEditView(
                    onReadMode = { },
                    viewModel = viewModel
                )
            }
        }

        composeTestRule
            .onNodeWithText("Description")
            .performTextInput(description)

        // Then
        capturedDescription mustBe description
    }

    @Test
    fun It_propagates_Alias_changes_of_the_ViewModel() {
        // Given
        val aliases: List<String> = fixture.listFixture(size = 5)

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermboxEditView(
                    onReadMode = { },
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
    fun Given_a_User_changes_a_Alias_it_delegates_the_call_to_the_ViewModel() {
        // Given
        val aliases: List<String> = fixture.listFixture(size = 5)
        val newAlias: String = fixture.fixture()
        val index = 3

        var capturedAlias: String? = null
        var capturedIndex: Int? = null

        this.aliases.update { aliases }

        viewModel.setAlias = { givenIndex, givenAlias ->
            capturedIndex = givenIndex
            capturedAlias = givenAlias
        }

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermboxEditView(
                    onReadMode = { },
                    viewModel = viewModel
                )
            }
        }

        composeTestRule
            .onNodeWithText(aliases[index])
            .performTextReplacement(newAlias)

        // Then
        capturedIndex mustBe index
        capturedAlias mustBe newAlias
    }

    @Test
    fun Given_a_User_enters_a_new_Alias_it_delegates_the_call_to_the_ViewModel() {
        // Given
        val aliases: List<String> = fixture.listFixture(size = 5)

        var capturedAlias: String? = null

        this.aliases.update { aliases }

        viewModel.addAlias = { givenAlias ->
            capturedAlias = givenAlias
        }

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermboxEditView(
                    onReadMode = { },
                    viewModel = viewModel
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Add a new alias")
            .performClick()

        // Then
        capturedAlias mustBe ""
    }

    @Test
    fun Given_a_User_clicks_cancel_it_discharge_changes_and_reverts_to_the_read_mode() {
        // Given
        var readModeWasCalled = false
        var cancelWasCalled = false

        val onReadMode = { readModeWasCalled = true }
        viewModel.dischargeChanges = { cancelWasCalled = true }

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermboxEditView(
                    onReadMode = onReadMode,
                    viewModel = viewModel
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Discard changes")
            .performClick()

        // Then
        cancelWasCalled mustBe true
        readModeWasCalled mustBe true
    }

    @Test
    fun Given_a_User_clicks_cancel_it_saves_changes_and_reverts_to_the_read_mode() {
        // Given
        var readModeWasCalled = false
        var saveWasCalled = false

        val onReadMode = { readModeWasCalled = true }
        viewModel.saveChanges = { saveWasCalled = true }

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermboxEditView(
                    onReadMode = onReadMode,
                    viewModel = viewModel
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Save changes")
            .performClick()

        // Then
        saveWasCalled mustBe true
        readModeWasCalled mustBe true
    }
}
