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
import androidx.compose.ui.test.performTextInput
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.mustBe
import tech.antibytes.wikidata.app.ui.theme.WikidataMobileTheme
import tech.antibytes.wikidata.app.util.UtilContract
import tech.antibytes.wikidata.mock.MwLocaleAndroidStub
import java.util.Locale
import java.util.Locale.ENGLISH

class LanguageSelectorScreenSpec {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val fixture = kotlinFixture()

    private val currentLanguage = MutableStateFlow<UtilContract.MwLocale>(
        MwLocaleAndroidStub(
            fixture.fixture(),
            fixture.fixture(),
            ENGLISH
        ),
    )
    private val selection = MutableStateFlow<List<UtilContract.MwLocale>>(
        listOf(
            currentLanguage.value,
            MwLocaleAndroidStub(
                fixture.fixture(),
                fixture.fixture(),
                Locale.GERMAN
            ),
            MwLocaleAndroidStub(
                fixture.fixture(),
                fixture.fixture(),
                Locale.KOREAN
            ),
            MwLocaleAndroidStub(
                fixture.fixture(),
                fixture.fixture(),
                Locale.JAPAN
            ),
        )
    )
    private val filter = MutableStateFlow("")

    private val viewModel = LanguageSelectorViewModelStub(currentLanguage, selection, filter)

    @Before
    fun setUp() {
        viewModel.clear()

        currentLanguage.update {
            MwLocaleAndroidStub(
                fixture.fixture(),
                fixture.fixture(),
                ENGLISH
            )
        }
        selection.update {
            listOf(
                currentLanguage.value,
                MwLocaleAndroidStub(
                    fixture.fixture(),
                    fixture.fixture(),
                    Locale.GERMAN
                ),
                MwLocaleAndroidStub(
                    fixture.fixture(),
                    fixture.fixture(),
                    Locale.KOREAN
                ),
                MwLocaleAndroidStub(
                    fixture.fixture(),
                    fixture.fixture(),
                    Locale.JAPAN
                ),
            )
        }
        filter.update { "" }
    }

    @Test
    fun It_renders_a_LanguageSelectorScreen() {
        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                LanguageSelectorScreen(
                    {},
                    viewModel
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("Find a language")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(currentLanguage.value.displayName)
            .assertIsDisplayed()
    }

    @Test
    fun It_propagates_Filter_changes_of_the_Viewmodel() {
        // Given
        val filter: String = fixture.fixture()

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                LanguageSelectorScreen(
                    {},
                    viewModel
                )
            }
        }

        this.filter.update { filter }

        // Then
        composeTestRule
            .onNodeWithText(filter)
            .assertIsDisplayed()
    }

    @Test
    fun It_propagates_Selection_changes_of_the_Viewmodel() {
        // Given
        val selection = listOf(
            MwLocaleAndroidStub(fixture.fixture(), fixture.fixture(), ENGLISH),
        )

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                LanguageSelectorScreen(
                    {},
                    viewModel
                )
            }
        }

        this.selection.update { selection }

        // Then
        composeTestRule
            .onNodeWithText(selection[0].displayName)
            .assertIsDisplayed()
    }

    @Test
    fun It_propagates_Selector_changes_of_the_Viewmodel() {
        // Given
        val selector = selection.value[1]

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                LanguageSelectorScreen(
                    {},
                    viewModel
                )
            }
        }

        currentLanguage.update { selector }

        runBlocking {
            delay(10)
        }

        // Then
        composeTestRule
            .onNodeWithText(selection.value[1].displayName)
            .onChildAt(0)
            .assertIsSelected()
    }

    @Test
    fun Given_a_LanguageFilter_is_entered_it_propagates_it_to_the_ViewModel() {
        // Given
        val filter: String = fixture.fixture()

        var capturedFilter: String? = null
        viewModel.setFilter = { givenFilter ->
            capturedFilter = givenFilter
        }

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                LanguageSelectorScreen(
                    {},
                    viewModel
                )
            }
        }

        composeTestRule
            .onNodeWithText("Find a language")
            .performTextInput(filter)

        // Then
        capturedFilter mustBe filter
    }

    @Test
    fun Given_a_LanguageItem_is_selected_it_propagates_it_to_the_ViewModel() {
        // Given
        val selection = listOf(
            MwLocaleAndroidStub(
                fixture.fixture(),
                fixture.fixture(),
                ENGLISH
            ),
            MwLocaleAndroidStub(
                fixture.fixture(),
                fixture.fixture(),
                Locale.GERMAN
            ),
            MwLocaleAndroidStub(
                fixture.fixture(),
                fixture.fixture(),
                Locale.KOREAN
            ),
            MwLocaleAndroidStub(
                fixture.fixture(),
                fixture.fixture(),
                Locale.JAPAN
            ),
        )

        this.selection.update { selection }

        var capturedSelector: Int? = null
        viewModel.selectLanguage = { givenSelector ->
            capturedSelector = givenSelector
        }

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                LanguageSelectorScreen(
                    {},
                    viewModel
                )
            }
        }

        composeTestRule
            .onNodeWithText(selection.last().displayName)
            .performClick()

        // Then
        capturedSelector mustBe selection.lastIndex
    }

    @Test
    fun Given_a_LanguageItem_is_selected_it_calls_the_Navigator() {
        // Given
        val selection = listOf(
            MwLocaleAndroidStub(
                fixture.fixture(),
                fixture.fixture(),
                ENGLISH
            ),
            MwLocaleAndroidStub(
                fixture.fixture(),
                fixture.fixture(),
                Locale.GERMAN
            ),
            MwLocaleAndroidStub(
                fixture.fixture(),
                fixture.fixture(),
                Locale.KOREAN
            ),
            MwLocaleAndroidStub(
                fixture.fixture(),
                fixture.fixture(),
                Locale.JAPAN
            ),
        )

        this.selection.update { selection }

        var wasCalled = false
        val navigator = LanguageSelectorContract.Navigator { wasCalled = true }

        viewModel.selectLanguage = {}

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                LanguageSelectorScreen(
                    navigator,
                    viewModel
                )
            }
        }

        composeTestRule
            .onNodeWithText(selection.last().displayName)
            .performClick()

        // Then
        wasCalled mustBe true
    }
}

private class LanguageSelectorViewModelStub(
    override val currentLanguage: StateFlow<UtilContract.MwLocale>,
    override val selection: StateFlow<List<UtilContract.MwLocale>>,
    override val filter: StateFlow<String>,
    var setFilter: ((String) -> Unit)? = null,
    var selectLanguage: ((Int) -> Unit)? = null
) : LanguageSelectorContract.LanguageSelectorViewModel {

    override fun setFilter(newFilter: String) {
        return setFilter?.invoke(newFilter)
            ?: throw RuntimeException("Missing Sideeffect setFilter")
    }

    override fun selectLanguage(selector: Int) {
        return selectLanguage?.invoke(selector)
            ?: throw RuntimeException("Missing Sideeffect selectLanguage")
    }

    fun clear() {
        setFilter = null
        selectLanguage = null
    }
}
