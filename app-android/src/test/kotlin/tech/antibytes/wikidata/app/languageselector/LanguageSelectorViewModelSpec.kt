/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.languageselector

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withTimeout
import kotlinx.datetime.Instant
import org.junit.Before
import org.junit.Test
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.util.coroutine.result.ResultContract
import tech.antibytes.util.coroutine.result.Success
import tech.antibytes.util.coroutine.wrapper.SharedFlowWrapper
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract
import tech.antibytes.wikidata.app.util.UtilContract
import tech.antibytes.wikidata.mock.EntityStoreStub
import tech.antibytes.wikidata.mock.MonolingualEntity
import tech.antibytes.wikidata.mock.MwLocaleStub

class LanguageSelectorViewModelSpec {
    private val fixture = kotlinFixture()
    private val currentLanguageState = MutableStateFlow<UtilContract.MwLocale>(
        MwLocaleStub(fixture.fixture(), fixture.fixture(), fixture.fixture())
    )

    private val entityFlow: MutableStateFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>> = MutableStateFlow(
        Success(
            MonolingualEntity(
                id = fixture.fixture(),
                type = EntityModelContract.EntityType.ITEM,
                revision = fixture.fixture(),
                language = fixture.fixture(),
                isEditable = fixture.fixture(),
                lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
                label = fixture.fixture(),
                description = fixture.fixture(),
                aliases = emptyList()
            )
        )
    )

    private val entitySurfaceFlow = SharedFlowWrapper.getInstance(
        entityFlow
    ) { CoroutineScope(Dispatchers.Default) }

    private val entityStore = EntityStoreStub(entitySurfaceFlow)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        currentLanguageState.value = MwLocaleStub(fixture.fixture(), fixture.fixture(), fixture.fixture())
        entityStore.clear()

        entityFlow.value = Success(
            MonolingualEntity(
                id = fixture.fixture(),
                type = EntityModelContract.EntityType.ITEM,
                revision = fixture.fixture(),
                language = fixture.fixture(),
                isEditable = fixture.fixture(),
                lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
                label = fixture.fixture(),
                description = fixture.fixture(),
                aliases = emptyList()
            )
        )

        Dispatchers.setMain(Dispatchers.Default)
    }

    @Test
    fun `It fulfils LanguageSelectorViewModel`() {
        val viewModel = LanguageSelectorViewModel(
            currentLanguageState,
            emptyList(),
            entityStore
        )
        viewModel fulfils LanguageSelectorContract.LanguageSelectorViewModel::class
        viewModel fulfils ViewModel::class
    }

    @Test
    fun `Its filter is empty by default`() {
        LanguageSelectorViewModel(
            currentLanguageState,
            emptyList(),
            entityStore
        ).filter.value mustBe ""
    }

    @Test
    fun `Its currentLanguage is the injected language by default`() {
        val expected = MwLocaleStub(fixture.fixture(), fixture.fixture(), fixture.fixture())

        LanguageSelectorViewModel(
            MutableStateFlow(expected),
            emptyList(),
            entityStore
        ).currentLanguage.value mustBe expected
    }

    @Test
    fun `Its selectedLanguages are the injected languages by default`() {
        val expected: List<UtilContract.MwLocale> = listOf(
            MwLocaleStub(fixture.fixture(), fixture.fixture(), fixture.fixture()),
            MwLocaleStub(fixture.fixture(), fixture.fixture(), fixture.fixture()),
            MwLocaleStub(fixture.fixture(), fixture.fixture(), fixture.fixture())
        )

        LanguageSelectorViewModel(
            currentLanguageState,
            expected,
            entityStore
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
            entityStore
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
        val selection: List<UtilContract.MwLocale> = listOf(
            MwLocaleStub(fixture.fixture(), fixture.fixture(), fixture.fixture()),
            MwLocaleStub(fixture.fixture(), fixture.fixture(), fixture.fixture()),
            MwLocaleStub(fixture.fixture(), fixture.fixture(), fixture.fixture())
        )

        val newFilterValue = selection[1].displayName
        val result = Channel<List<UtilContract.MwLocale>>()

        // When
        val viewModel = LanguageSelectorViewModel(
            currentLanguageState,
            selection,
            entityStore
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
                result.receive() mustBe listOf(selection[1])
            }
        }
    }

    @Test
    fun `Given setFilter is called is changes the selection, while normalizing the displayName`() {
        // Given
        val selection: List<UtilContract.MwLocale> = listOf(
            MwLocaleStub(fixture.fixture(), fixture.fixture<String>().uppercase(), fixture.fixture()),
            MwLocaleStub(fixture.fixture(), fixture.fixture<String>().uppercase(), fixture.fixture()),
            MwLocaleStub(fixture.fixture(), fixture.fixture<String>().uppercase(), fixture.fixture())
        )

        val newFilterValue = selection[1].displayName
        val result = Channel<List<UtilContract.MwLocale>>()

        // When
        val viewModel = LanguageSelectorViewModel(
            currentLanguageState,
            selection,
            entityStore
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
                result.receive() mustBe listOf(selection[1])
            }
        }
    }

    @Test
    fun `Given setFilter is called with a empty String it reverts the selection`() {
        // Given
        val selection: List<UtilContract.MwLocale> = listOf(
            MwLocaleStub(fixture.fixture(), fixture.fixture(), fixture.fixture()),
            MwLocaleStub(fixture.fixture(), fixture.fixture(), fixture.fixture()),
            MwLocaleStub(fixture.fixture(), fixture.fixture(), fixture.fixture())
        )

        val newFilterValue = selection[1].displayName
        val result = Channel<List<UtilContract.MwLocale>>()

        // When
        val viewModel = LanguageSelectorViewModel(
            currentLanguageState,
            selection,
            entityStore
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
                result.receive() mustBe listOf(selection[1])
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
    fun `Given selectLanguage is called with a Selector it changes the currentLanguage and fetches the Entity in that language`() {
        // Given
        val selection: List<UtilContract.MwLocale> = listOf(
            MwLocaleStub(fixture.fixture(), fixture.fixture(), fixture.fixture()),
            MwLocaleStub(fixture.fixture(), fixture.fixture(), fixture.fixture()),
            MwLocaleStub(fixture.fixture(), fixture.fixture(), fixture.fixture())
        )

        val selector = 0
        val result = Channel<UtilContract.MwLocale>()

        val entity = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            isEditable = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = emptyList()
        )

        entityFlow.update { Success(entity) }

        var capturedId: String? = null
        var capturedLanguage: String? = null
        entityStore.fetchEntity = { givenId, givenLanguage ->
            capturedId = givenId
            capturedLanguage = givenLanguage
        }

        // When
        val viewModel = LanguageSelectorViewModel(
            currentLanguageState,
            selection,
            entityStore
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

        capturedId mustBe entity.id
        capturedLanguage mustBe selection[selector].toLanguageTag()
    }
}
