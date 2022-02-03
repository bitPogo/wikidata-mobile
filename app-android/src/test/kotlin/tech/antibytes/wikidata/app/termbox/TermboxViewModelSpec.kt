/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termbox

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.yield
import kotlinx.datetime.Instant
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import tech.antibytes.util.coroutine.result.Failure
import tech.antibytes.util.coroutine.result.ResultContract
import tech.antibytes.util.coroutine.result.Success
import tech.antibytes.util.coroutine.wrapper.SharedFlowWrapper
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fixture.listFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract
import tech.antibytes.wikibase.store.entity.lang.EntityStoreError
import tech.antibytes.wikidata.app.BuildConfig
import tech.antibytes.wikidata.mock.EntityStoreStub
import tech.antibytes.wikidata.mock.MonolingualEntity
import tech.antibytes.wikidata.mock.MwLocaleStub
import tech.antibytes.wikidata.mock.PageStoreStub
import tech.antibytes.wikidata.mock.QrCodeStoreStub
import java.lang.Exception

@RunWith(RobolectricTestRunner::class)
class TermboxViewModelSpec {
    private val fixture = kotlinFixture()
    private val currentLanguageState = MutableStateFlow(
        MwLocaleStub(fixture.fixture(), fixture.fixture(), fixture.fixture())
    )

    private val entityFlow: MutableStateFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>> = MutableStateFlow(
        Failure(EntityStoreError.InitialState())
    )

    private val entitySurfaceFlow = SharedFlowWrapper.getInstance(
        entityFlow
    ) { CoroutineScope(Dispatchers.Default) }

    private val pageFlow: MutableSharedFlow<ResultContract<String, Exception>> = MutableSharedFlow()

    private val pageSurfaceFlow = SharedFlowWrapper.getInstance(
        pageFlow
    ) { CoroutineScope(Dispatchers.Default) }

    private val qrCodeFlow: MutableSharedFlow<ResultContract<Bitmap, Exception>> = MutableSharedFlow()

    private val qrCodeSurfaceFlow = SharedFlowWrapper.getInstance(
        qrCodeFlow
    ) { CoroutineScope(Dispatchers.Default) }

    private val entityStore = EntityStoreStub(
        entitySurfaceFlow,
    )
    private val pageStore = PageStoreStub(
        pageSurfaceFlow,
        SharedFlowWrapper.getInstance(MutableSharedFlow()) { CoroutineScope(Dispatchers.Default) }
    )

    private val qrCodeStore = QrCodeStoreStub(
        qrCodeSurfaceFlow
    )

    @Before
    fun setUp() {
        entityStore.clear()
        pageStore.clear()
        qrCodeStore.clear()

        entityStore.fetchEntity = { _, _ -> Unit }
        qrCodeStore.fetch = { }

        entityFlow.update { Failure(EntityStoreError.InitialState()) }
    }

    @Test
    fun `It fulfils TermboxViewModel`() {
        val viewModel = TermboxViewModel(
            currentLanguageState,
            entityStore,
            pageStore,
            qrCodeStore
        )
        viewModel fulfils TermboxContract.TermboxViewModel::class
        viewModel fulfils ViewModel::class
    }

    @Test
    fun `It fetches an Entity on initialisation`() {
        // Given
        var capturedId: String? = null
        var capturedLanguageTag: String? = null
        entityStore.fetchEntity = { givenId, givenLanguage ->
            capturedId = givenId
            capturedLanguageTag = givenLanguage
        }

        // When
        TermboxViewModel(
            currentLanguageState,
            entityStore,
            pageStore,
            qrCodeStore
        )

        runBlocking {
            delay(200)
        }

        // Then
        capturedLanguageTag mustBe currentLanguageState.value.toLanguageTag()
        capturedId mustBe "Q214750"
    }

    @Test
    fun `It will not fetches an Entity on initialisation, if the Store is already in use`() {
        // Given
        var capturedId: String? = null
        var capturedLanguageTag: String? = null
        entityStore.fetchEntity = { givenId, givenLanguage ->
            capturedId = givenId
            capturedLanguageTag = givenLanguage
        }

        entityFlow.update { Failure(RuntimeException()) }

        // When
        TermboxViewModel(
            currentLanguageState,
            entityStore,
            pageStore,
            qrCodeStore
        )

        // Then
        capturedLanguageTag mustBe null
        capturedId mustBe null
    }

    @Test
    fun `It has an empty Id by default`() {
        // When
        val viewModel = TermboxViewModel(
            currentLanguageState,
            entityStore,
            pageStore,
            qrCodeStore
        )

        // Then
        viewModel.id.value mustBe ""
    }

    @Test
    fun `It distributes an emitted Id`() {
        // Given
        val id: String = fixture.fixture()
        val result = Channel<String>()

        val entity = MonolingualEntity(
            id = id,
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            isEditable = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = emptyList()
        )

        // When
        val viewModel = TermboxViewModel(
            currentLanguageState,
            entityStore,
            pageStore,
            qrCodeStore
        )

        CoroutineScope(Dispatchers.Default).launch {
            viewModel.id.collectLatest {
                result.send(it)
            }
        }

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() mustBe ""
            }
        }

        // When
        entityFlow.update { Success(entity) }

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() mustBe id
            }
        }
    }

    @Test
    fun `It has an null for an QrCode by default`() {
        // When
        val viewModel = TermboxViewModel(
            currentLanguageState,
            entityStore,
            pageStore,
            qrCodeStore
        )

        // Then
        viewModel.qrCode.value mustBe null
    }

    @Test
    fun `It distributes an emitted QrCode`() {
        // Given
        val qrCode = Bitmap.createBitmap(2, 2, Bitmap.Config.RGB_565)
        val result = Channel<Bitmap?>()

        // When
        val viewModel = TermboxViewModel(
            currentLanguageState,
            entityStore,
            pageStore,
            qrCodeStore
        )

        CoroutineScope(Dispatchers.Default).launch {
            viewModel.qrCode.collectLatest {
                result.send(it)
            }
        }

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() mustBe null
            }
        }

        // When
        CoroutineScope(Dispatchers.Default).launch {
            qrCodeFlow.emit(Success(qrCode))
        }

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() sameAs qrCode
            }
        }
    }

    @Test
    fun `It emits the given Language`() {
        // Given
        val language = MwLocaleStub(fixture.fixture(), fixture.fixture(), fixture.fixture())

        currentLanguageState.value = language

        // When
        val viewModel = TermboxViewModel(
            currentLanguageState,
            entityStore,
            pageStore,
            qrCodeStore
        )

        // Then
        viewModel.language.value mustBe language
    }

    @Test
    fun `It is not editable by default`() {
        // When
        val viewModel = TermboxViewModel(
            currentLanguageState,
            entityStore,
            pageStore,
            qrCodeStore
        )

        // Then
        viewModel.isEditable.value mustBe false
    }

    @Test
    fun `It distributes an emitted Edibility`() {
        // Given
        val edibility = true
        val result = Channel<Boolean>()

        val entity = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            isEditable = edibility,
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = emptyList()
        )

        // When
        val viewModel = TermboxViewModel(
            currentLanguageState,
            entityStore,
            pageStore,
            qrCodeStore
        )

        CoroutineScope(Dispatchers.Default).launch {
            viewModel.isEditable.collectLatest {
                result.send(it)
            }
        }

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() mustBe false
            }
        }

        // When
        entityFlow.update { Success(entity) }

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() mustBe edibility
            }
        }
    }

    @Test
    fun `It has a empty Label by default`() {
        // When
        val viewModel = TermboxViewModel(
            currentLanguageState,
            entityStore,
            pageStore,
            qrCodeStore
        )

        // Then
        viewModel.label.value mustBe ""
    }

    @Test
    fun `It distributes an emitted Label`() {
        // Given
        val label: String = fixture.fixture()
        val result = Channel<String>()

        val entity = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            isEditable = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            label = label,
            description = fixture.fixture(),
            aliases = emptyList()
        )

        // When
        val viewModel = TermboxViewModel(
            currentLanguageState,
            entityStore,
            pageStore,
            qrCodeStore
        )

        CoroutineScope(Dispatchers.Default).launch {
            viewModel.label.collectLatest {
                result.send(it)
            }
        }

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() mustBe ""
            }
        }

        // When
        entityFlow.update { Success(entity) }

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() mustBe label
            }
        }
    }

    @Test
    fun `It distributes an empty Label if the Entities Label is null`() {
        // Given
        val result = Channel<String>()

        val entity = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            isEditable = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            label = null,
            description = fixture.fixture(),
            aliases = emptyList()
        )

        // When
        val viewModel = TermboxViewModel(
            currentLanguageState,
            entityStore,
            pageStore,
            qrCodeStore
        )

        CoroutineScope(Dispatchers.Default).launch {
            viewModel.label.collectLatest {
                result.send(it)
            }
        }

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() mustBe ""
            }
        }

        // When
        entityFlow.update { Success(entity.copy(label = fixture.fixture<String>())) } // Trigger refresh
        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() // Ignore Refresh
            }
        }

        // When
        entityFlow.update { Success(entity) }

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() mustBe ""
            }
        }
    }

    @Test
    fun `It has a empty Description by default`() {
        // When
        val viewModel = TermboxViewModel(
            currentLanguageState,
            entityStore,
            pageStore,
            qrCodeStore
        )

        // Then
        viewModel.description.value mustBe ""
    }

    @Test
    fun `It distributes an emitted Description`() {
        // Given
        val description: String = fixture.fixture()
        val result = Channel<String>()

        val entity = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            isEditable = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            label = fixture.fixture(),
            description = description,
            aliases = emptyList()
        )

        // When
        val viewModel = TermboxViewModel(
            currentLanguageState,
            entityStore,
            pageStore,
            qrCodeStore
        )

        CoroutineScope(Dispatchers.Default).launch {
            viewModel.description.collectLatest {
                result.send(it)
            }
        }

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() mustBe ""
            }
        }

        // When
        entityFlow.update { Success(entity) }

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() mustBe description
            }
        }
    }

    @Test
    fun `It distributes an empty Label if the Entities Description is null`() {
        // Given
        val result = Channel<String>()

        val entity = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            isEditable = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            label = fixture.fixture(),
            description = null,
            aliases = emptyList()
        )

        // When
        val viewModel = TermboxViewModel(
            currentLanguageState,
            entityStore,
            pageStore,
            qrCodeStore
        )

        CoroutineScope(Dispatchers.Default).launch {
            viewModel.description.collectLatest {
                result.send(it)
            }
        }

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() mustBe ""
            }
        }

        // When
        entityFlow.update { Success(entity.copy(description = fixture.fixture<String>())) } // Trigger refresh
        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() // Ignore Refresh
            }
        }

        // When
        entityFlow.update { Success(entity) }

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() mustBe ""
            }
        }
    }

    @Test
    fun `It has a empty List of Alias by default`() {
        // When
        val viewModel = TermboxViewModel(
            currentLanguageState,
            entityStore,
            pageStore,
            qrCodeStore
        )

        // Then
        viewModel.aliases.value mustBe emptyList()
    }

    @Test
    fun `It distributes an emitted Aliases`() {
        // Given
        val aliases: List<String> = fixture.listFixture(size = 5)
        val result = Channel<List<String>>()

        val entity = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            isEditable = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = aliases
        )

        // When
        val viewModel = TermboxViewModel(
            currentLanguageState,
            entityStore,
            pageStore,
            qrCodeStore
        )

        CoroutineScope(Dispatchers.Default).launch {
            viewModel.aliases.collectLatest {
                result.send(it)
            }
        }

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() mustBe emptyList()
            }
        }

        // When
        entityFlow.update { Success(entity) }

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() mustBe aliases
            }
        }
    }

    @Test
    fun `Given setLabel is called with a new Label it delegates the call to EntityStore`() {
        // Given
        val newLabel: String = fixture.fixture()

        var capturedLabel: String? = null
        entityStore.setLabel = { givenLabel ->
            capturedLabel = givenLabel
        }

        // When
        TermboxViewModel(
            currentLanguageState,
            entityStore,
            pageStore,
            qrCodeStore
        ).setLabel(newLabel)

        // Then
        capturedLabel mustBe newLabel
    }

    @Test
    fun `Given setDescription is called with a new Description it delegates the call to EntityStore`() {
        // Given
        val newDescription: String = fixture.fixture()

        var capturedDescription: String? = null
        entityStore.setDescription = { givenDescription ->
            capturedDescription = givenDescription
        }

        // When
        TermboxViewModel(
            currentLanguageState,
            entityStore,
            pageStore,
            qrCodeStore
        ).setDescription(newDescription)

        // Then
        capturedDescription mustBe newDescription
    }

    @Test
    fun `Given setAlias is called with an Index new Alias it delegates the call to EntityStore`() {
        // Given
        val index: Int = fixture.fixture()
        val newAlias: String = fixture.fixture()

        var capturedIndex: Int? = null
        var capturedAlias: String? = null
        entityStore.setAlias = { givenIdx, givenAlias ->
            capturedIndex = givenIdx
            capturedAlias = givenAlias
        }

        // When
        TermboxViewModel(
            currentLanguageState,
            entityStore,
            pageStore,
            qrCodeStore
        ).setAlias(index, newAlias)

        // Then
        capturedIndex mustBe index
        capturedAlias mustBe newAlias
    }

    @Test
    fun `Given addAlias is called with an new Alias it delegates the call to EntityStore, while appending the new Alias`() {
        // Given
        val aliases: List<String> = fixture.listFixture()
        val newAlias: String = fixture.fixture()

        var capturedAliases: List<String>? = null
        entityStore.setAliases = { givenAliases ->
            capturedAliases = givenAliases
        }

        // When
        val viewModel = TermboxViewModel(
            currentLanguageState,
            entityStore,
            pageStore,
            qrCodeStore
        )

        entityFlow.update {
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
                    aliases = aliases
                )
            )
        }

        runBlocking {
            yield()
        }

        viewModel.addAlias(newAlias)

        runBlocking {
            yield()
        }

        // Then
        capturedAliases mustBe aliases.toMutableList().also {
            it.add(newAlias)
        }
    }

    @Test
    fun `Given dischargeChanges is called it delegates the call to EntityStore`() {
        // Given
        var wasCalled = false
        entityStore.rollback = {
            wasCalled = true
        }

        // When
        TermboxViewModel(
            currentLanguageState,
            entityStore,
            pageStore,
            qrCodeStore
        ).dischargeChanges()

        // Then
        wasCalled mustBe true
    }

    @Test
    fun `Given saveChanges is called it delegates the call to EntityStore`() {
        // Given
        var wasCalled = false
        entityStore.save = {
            wasCalled = true
        }

        // When
        TermboxViewModel(
            currentLanguageState,
            entityStore,
            pageStore,
            qrCodeStore
        ).saveChanges()

        // Then
        wasCalled mustBe true
    }

    @Test
    fun `Given refresh is called it delegates the call to EntityStore`() {
        // Given
        var wasCalled = false
        entityStore.save = {
            wasCalled = true
        }

        // When
        TermboxViewModel(
            currentLanguageState,
            entityStore,
            pageStore,
            qrCodeStore
        ).saveChanges()

        // Then
        wasCalled mustBe true
    }

    @Test
    fun `Given fetchItem is called with an Id it delegates, while using the currentLanguage the call to EntityStore`() {
        // Given
        val id: String = fixture.fixture()
        val language = MwLocaleStub(fixture.fixture(), fixture.fixture(), fixture.fixture())

        currentLanguageState.update { language }

        var capturedId: String? = null
        var capturedLanguageTag: String? = null

        entityStore.fetchEntity = { givenId, givenLanguage ->
            capturedId = givenId
            capturedLanguageTag = givenLanguage
        }

        // When
        TermboxViewModel(
            currentLanguageState,
            entityStore,
            pageStore,
            qrCodeStore
        ).fetchItem(id)

        // Then
        capturedLanguageTag mustBe language.toLanguageTag()
        capturedId mustBe id
    }

    @Test
    fun `Given createNewItem is called it delegates, while using the currentLanguage the call to EntityStore`() {
        // Given
        val language = MwLocaleStub(fixture.fixture(), fixture.fixture(), fixture.fixture())

        currentLanguageState.update { language }

        var capturedLanguageTag: String? = null
        var capturedType: EntityModelContract.EntityType? = null

        entityStore.create = { givenLanguage, givenType ->
            capturedLanguageTag = givenLanguage
            capturedType = givenType
        }

        // When
        TermboxViewModel(
            currentLanguageState,
            entityStore,
            pageStore,
            qrCodeStore
        ).createNewItem()

        // Then
        capturedLanguageTag mustBe language.toLanguageTag()
        capturedType mustBe EntityModelContract.EntityType.ITEM
    }

    @Test
    fun `Given randomItem is called it calls the PageStore and fetches the Entity for the given Id`() {
        // Given
        val id: String = fixture.fixture()
        val language = MwLocaleStub(fixture.fixture(), fixture.fixture(), fixture.fixture())

        currentLanguageState.update { language }

        var capturedId: String? = null
        var capturedLanguageTag: String? = null

        pageStore.fetchRandomItem = {
            pageFlow.tryEmit(Success(id))
        }

        entityStore.fetchEntity = { givenId, givenLanguage ->
            capturedId = givenId
            capturedLanguageTag = givenLanguage
        }

        // When
        TermboxViewModel(
            currentLanguageState,
            entityStore,
            pageStore,
            qrCodeStore
        ).fetchItem(id)

        // Then
        capturedLanguageTag mustBe language.toLanguageTag()
        capturedId mustBe id
    }

    @Test
    fun `Given a Id has been changed it updates the QrCode if the Id is not empty`() {
        // Given
        val id: String = fixture.fixture()
        val result = Channel<String>()

        val entity = MonolingualEntity(
            id = id,
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            isEditable = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = emptyList()
        )

        var capturedUrl: String? = null
        qrCodeStore.fetch = { givenUrl ->
            capturedUrl = givenUrl
        }

        // When
        val viewModel = TermboxViewModel(
            currentLanguageState,
            entityStore,
            pageStore,
            qrCodeStore
        )

        CoroutineScope(Dispatchers.Default).launch {
            viewModel.id.collectLatest {
                result.send(it)
            }
        }

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive()
            }
        }

        // When
        entityFlow.update { Success(entity) }

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive()
                capturedUrl mustBe "https://${BuildConfig.ENDPOINT}/wiki/$id"
            }
        }
    }

    @Test
    fun `Given a Id has been changed it updates the QrCode to null if the Id is empty`() {
        // Given
        val id = ""
        val bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.RGB_565)
        val result = Channel<Bitmap?>()

        val entity = MonolingualEntity(
            id = id,
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            isEditable = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = emptyList()
        )

        // When
        val viewModel = TermboxViewModel(
            currentLanguageState,
            entityStore,
            pageStore,
            qrCodeStore
        )

        CoroutineScope(Dispatchers.Default).launch {
            viewModel.qrCode.collectLatest {
                result.send(it)
            }
        }

        val job = CoroutineScope(Dispatchers.Default).launch {
            qrCodeFlow.emit(Success(bitmap))
        }

        // Then
        runBlocking {
            withTimeout(2000) {
                job.join()
                result.receive() mustBe bitmap
            }
        }

        // When
        entityFlow.update { Success(entity) }

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() mustBe null
            }
        }
    }
}
