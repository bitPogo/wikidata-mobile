/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termsearch

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.junit.Rule
import org.junit.Test
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.mustBe
import tech.antibytes.wikibase.store.page.domain.model.EntityId
import tech.antibytes.wikibase.store.page.domain.model.LanguageTag
import tech.antibytes.wikibase.store.page.domain.model.PageModelContract
import tech.antibytes.wikidata.app.ui.theme.WikidataMobileTheme

class TermSearchScreenSpec {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val fixture = kotlinFixture()

    private val query = MutableStateFlow("")
    private val result = MutableStateFlow(emptyList<PageModelContract.SearchEntry>())

    private val viewModel = TermSearchViewModelStub(result, query)

    @Test
    fun It_renders_a_TermSearchScreen() {
        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermSearchScreen(
                    {},
                    viewModel
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("Your query")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Please enter a query")
            .assertIsDisplayed()
    }

    @Test
    fun It_propagest_Query_changes_from_the_ViewModel() {
        // Given
        val query: String = fixture.fixture()

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermSearchScreen(
                    {},
                    viewModel
                )
            }
        }

        this.query.update { query }

        // Then
        composeTestRule
            .onNodeWithText(query)
            .assertIsDisplayed()
    }

    @Test
    fun It_propagest_User_input_of_a_Query_to_the_ViewModel() {
        // Given
        val query: String = fixture.fixture()

        var capturedQuery: String? = null
        viewModel.setQuery = { givenQuery ->
            capturedQuery = givenQuery
        }
        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermSearchScreen(
                    {},
                    viewModel
                )
            }
        }

        composeTestRule
            .onNodeWithText("Your query")
            .performTextInput(query)

        // Then
        capturedQuery mustBe query
    }

    @Test
    fun It_propagest_a_Result_changes_from_the_ViewModel() {
        // Given
        val label: String = fixture.fixture()
        val result = listOf(
            SearchEntry(
                label = label
            )
        )

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermSearchScreen(
                    {},
                    viewModel
                )
            }
        }

        this.result.update { result }

        // Then
        composeTestRule
            .onNodeWithText(label)
            .assertIsDisplayed()
    }

    @Test
    fun It_renders_a_Result_without_a_label() {
        // Given
        val result = listOf(
            SearchEntry(
                description = fixture.fixture<String>()
            )
        )

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermSearchScreen(
                    {},
                    viewModel
                )
            }
        }

        this.result.update { result }

        // Then
        composeTestRule
            .onNodeWithText("No label defined")
            .assertIsDisplayed()
    }

    @Test
    fun It_renders_a_Result_without_a_description() {
        // Given
        val result = listOf(
            SearchEntry(
                label = fixture.fixture<String>()
            )
        )

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermSearchScreen(
                    {},
                    viewModel
                )
            }
        }

        this.result.update { result }

        // Then
        composeTestRule
            .onNodeWithText("No description defined")
            .assertIsDisplayed()
    }

    @Test
    fun Given_a_User_clicks_the_search_Icon_it_delegates_the_call_to_the_ViewModel() {
        // Given
        var wasCalled = false
        viewModel.search = { wasCalled = true }

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermSearchScreen(
                    {},
                    viewModel
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Apply your query")
            .performClick()

        // Then
        wasCalled mustBe true
    }

    @Test
    fun Given_a_User_clicks_the_search_Icon_and_the_query_has_no_results_it_changes_the_info_text() {
        // Given
        viewModel.search = {}

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermSearchScreen(
                    {},
                    viewModel
                )
            }
        }

        query.update { fixture.fixture() }

        composeTestRule
            .onNodeWithContentDescription("Apply your query")
            .performClick()

        // Then
        composeTestRule
            .onNodeWithText("The given query had no results")
            .assertIsDisplayed()
    }

    @Test
    fun Given_a_User_clicks_on_a_result_it_delegates_the_call_to_the_ViewModel() {
        // Given
        val result = listOf(
            SearchEntry(
                label = fixture.fixture<String>()
            ),
            SearchEntry(
                label = fixture.fixture<String>()
            ),
            SearchEntry(
                label = fixture.fixture<String>()
            )
        )
        val expected = 1

        var capturedIndex: Int? = null
        viewModel.select = { idx ->
            capturedIndex = idx
        }

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermSearchScreen(
                    {},
                    viewModel
                )
            }
        }

        this.result.update { result }

        composeTestRule
            .onNodeWithText(result[expected].label!!)
            .performClick()

        // Then
        capturedIndex mustBe expected
    }

    @Test
    fun Given_a_User_clicks_on_a_result_it_calls_the_Navigator() {
        // Given
        val result = listOf(
            SearchEntry(
                label = fixture.fixture<String>()
            ),
            SearchEntry(
                label = fixture.fixture<String>()
            ),
            SearchEntry(
                label = fixture.fixture<String>()
            )
        )
        val expected = 1

        var wasCalled = false
        val navigator = TermSearchContract.Navigator { wasCalled = true }

        viewModel.select = { }

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermSearchScreen(
                    navigator,
                    viewModel
                )
            }
        }

        this.result.update { result }

        composeTestRule
            .onNodeWithText(result[expected].label!!)
            .performClick()

        // Then
        wasCalled mustBe true
    }

    @Test
    fun Given_a_User_clicks_on_a_result_it_reverts_it_info_text() {
        // Given
        val result = listOf(
            SearchEntry(
                label = fixture.fixture<String>()
            ),
            SearchEntry(
                label = fixture.fixture<String>()
            ),
            SearchEntry(
                label = fixture.fixture<String>()
            )
        )
        val expected = 1

        viewModel.select = { }
        query.update { fixture.fixture() }

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                TermSearchScreen(
                    { },
                    viewModel
                )
            }
        }

        this.result.update { result }

        composeTestRule
            .onNodeWithText(result[expected].label!!)
            .performClick()

        this.result.update { emptyList() }

        // Then
        composeTestRule
            .onNodeWithText("Please enter a query")
            .assertIsDisplayed()
    }
}

private class TermSearchViewModelStub(
    override val result: StateFlow<List<PageModelContract.SearchEntry>>,
    override val query: StateFlow<String>,
    var setQuery: ((String) -> Unit)? = null,
    var search: (() -> Unit)? = null,
    var select: ((Int) -> Unit)? = null
) : TermSearchContract.TermSearchViewModel {
    override fun setQuery(query: String) {
        return setQuery?.invoke(query)
            ?: throw RuntimeException("Missing Sideeffect setQuery")
    }

    override fun search() {
        return search?.invoke()
            ?: throw RuntimeException("Missing Sideeffect search")
    }

    override fun select(index: Int) {
        return select?.invoke(index)
            ?: throw RuntimeException("Missing Sideeffect select")
    }
}

private data class SearchEntry(
    override val id: EntityId = "test",
    override val language: LanguageTag = "de",
    override val label: String? = null,
    override val description: String? = null
) : PageModelContract.SearchEntry
