/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.languageselector

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Before
import org.junit.Test
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import java.util.Locale

class LanguageSelectorViewModelSpec {
    private val currentLanguageState = MutableStateFlow(Locale.ENGLISH)
    private val scope = CoroutineScope(Dispatchers.Default)

    @Before
    fun setUp() {
        currentLanguageState.value = Locale.ENGLISH
    }

    @Test
    fun `It fulfils LanguageSelectorViewModel`() {
        val viewModel = LanguageSelectorViewModel(
            currentLanguageState,
            emptyList(),
            scope
        )
        viewModel fulfils LanguageSelectorContract.LanguageSelectorViewModel::class
        viewModel fulfils ViewModel::class
    }

    @Test
    fun `Its filter is empty by default`() {
        LanguageSelectorViewModel(
            currentLanguageState,
            emptyList(),
            scope
        ).filter.value mustBe ""
    }

    @Test
    fun `Its currentLanguage is the injected language by default`() {
        val expected = Locale.JAPAN

        LanguageSelectorViewModel(
            MutableStateFlow(expected),
            emptyList(),
            scope
        ).currentLanguage.value mustBe expected
    }

    @Test
    fun `Its selectedLanguages are the injected languages by default`() {
        val expected = listOf(
            Locale.CANADA,
            Locale.CHINESE,
            Locale.ENGLISH
        )

        LanguageSelectorViewModel(
            currentLanguageState,
            expected,
            scope
        ).selection.value mustBe expected
    }

    @Test
    fun `Given setFilter is called is changes the filter`() {
        // Given
        val newFilterValue = "Eng"
        val result = Channel<String>()

        // When
        val viewModel = LanguageSelectorViewModel(
            currentLanguageState,
            emptyList(),
            scope
        )

        CoroutineScope(Dispatchers.Default).launch {
            viewModel.filter.collectLatest { givenFilter ->
                result.send(givenFilter)
            }
        }

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() mustBe ""
            }
        }

        // When
        viewModel.setFilter(newFilterValue)

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() mustBe newFilterValue
            }
        }
    }

    @Test
    fun `Given setFilter is called is changes the selection`() {
        // Given
        val newFilterValue = "Eng"
        val selection = listOf(
            Locale.KOREA,
            Locale.CHINESE,
            Locale.ENGLISH
        )
        val result = Channel<List<Locale>>()

        // When
        val viewModel = LanguageSelectorViewModel(
            currentLanguageState,
            selection,
            scope
        )

        CoroutineScope(Dispatchers.Default).launch {
            viewModel.selection.collectLatest { givenSelection ->
                result.send(givenSelection)
            }
        }

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() mustBe selection
            }
        }

        // When
        viewModel.setFilter(newFilterValue)

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() mustBe listOf(Locale.ENGLISH)
            }
        }
    }

    @Test
    fun `Given setFilter is called with a empty String it reverts the selection`() {
        // Given
        val newFilterValue = "Eng"
        val selection = listOf(
            Locale.KOREA,
            Locale.CHINESE,
            Locale.ENGLISH
        )
        val result = Channel<List<Locale>>()

        // When
        val viewModel = LanguageSelectorViewModel(
            currentLanguageState,
            selection,
            scope
        )

        CoroutineScope(Dispatchers.Default).launch {
            viewModel.selection.collectLatest { givenSelection ->
                result.send(givenSelection)
            }
        }

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() mustBe selection
            }
        }

        // When
        viewModel.setFilter(newFilterValue)

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() mustBe listOf(Locale.ENGLISH)
            }
        }

        // When
        viewModel.setFilter("")

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() mustBe selection
            }
        }
    }

    @Test
    fun `Given selectLanguage is called with a Selector it changes the currentLanguage`() {
        // Given
        val selection = listOf(
            Locale.KOREA,
            Locale.CHINESE,
            Locale.ENGLISH
        )
        val selector = 0
        val result = Channel<Locale>()

        // When
        val viewModel = LanguageSelectorViewModel(
            currentLanguageState,
            selection,
            scope
        )

        CoroutineScope(Dispatchers.Default).launch {
            viewModel.currentLanguage.collectLatest { givenLanguage ->
                result.send(givenLanguage)
            }
        }

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() mustBe currentLanguageState.value
            }
        }

        // When
        viewModel.selectLanguage(selector)

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() mustBe selection[selector]
            }
        }
    }
}
