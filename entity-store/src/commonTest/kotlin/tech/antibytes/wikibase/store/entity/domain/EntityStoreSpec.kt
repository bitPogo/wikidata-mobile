/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withTimeout
import kotlinx.datetime.Instant
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import tech.antibytes.util.coroutine.result.Failure
import tech.antibytes.util.coroutine.result.ResultContract
import tech.antibytes.util.coroutine.result.Success
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract
import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.coroutine.runBlockingTestWithTimeout
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fixture.listFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs
import tech.antibytes.wikibase.store.entity.EntityStoreContract
import tech.antibytes.wikibase.store.entity.domain.model.EntityId
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract
import tech.antibytes.wikibase.store.entity.domain.model.LanguageTag
import tech.antibytes.wikibase.store.entity.domain.model.MonolingualEntity
import tech.antibytes.wikibase.store.entity.lang.EntityStoreError
import tech.antibytes.wikibase.store.entity.testScope1
import tech.antibytes.wikibase.store.entity.testScope2
import tech.antibytes.wikibase.store.mock.EntityQueriesStub
import tech.antibytes.wikibase.store.mock.MwClientStub
import tech.antibytes.wikibase.store.mock.SharedFlowWrapperStub
import tech.antibytes.wikibase.store.mock.domain.RepositoryStub
import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.assertFailsWith

class EntityStoreSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils EntityStoreFactory`() {
        EntityStore fulfils EntityStoreContract.EntityStoreFactory::class
    }

    @Test
    fun `Given getInstance is called with its appropriate Parameter it returns a EntityStore`() {
        // When
        val store = EntityStore.getInstance(
            MwClientStub(),
            EntityQueriesStub(),
            { CoroutineScope(Dispatchers.Default) },
            { CoroutineScope(Dispatchers.Default) }
        )

        // Then
        store fulfils EntityStoreContract.EntityStore::class
    }

    @Test
    fun `Given fetchEntity is called it mutates the Entity as Success with the locally stored Entity`() {
        // Given
        val flow = MutableStateFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>>(
            Failure(EntityStoreError.InitialState())
        )
        val result = Channel<ResultContract<EntityModelContract.MonolingualEntity, Exception>>()

        val id: EntityId = fixture.fixture()
        val language: LanguageTag = fixture.fixture()

        val expected = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = fixture.listFixture(),
        )

        val localRepository = RepositoryStub()

        var capturedId: String? = null
        var capturedLanguage: String? = null
        localRepository.fetchEntity = { givenId, givenLanguage ->
            capturedId = givenId
            capturedLanguage = givenLanguage

            expected
        }

        val koin = koinApplication {
            modules(
                module {
                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.LOCAL)) {
                        localRepository
                    }

                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.REMOTE)) {
                        RepositoryStub()
                    }

                    single(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { testScope1 }
                    }

                    single {
                        flow
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<EntityModelContract.MonolingualEntity, Exception>> {
                        SharedFlowWrapperStub()
                    }
                }
            )
        }

        flow.onEach { item ->
            if (item.error !is EntityStoreError.InitialState) {
                result.send(item)
            }
        }.launchIn(testScope2)

        // When
        EntityStore(koin).fetchEntity(id, language)

        // Then
        runBlockingTestWithTimeout {
            result.receive().unwrap() mustBe expected
            capturedId mustBe id
            capturedLanguage mustBe language
        }
    }

    @Test
    fun `Given fetchEntity is called it mutates the Entity as Success with the remote fetched Entity, while updating the local stored one`() {
        // Given
        val flow = MutableStateFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>>(
            Failure(EntityStoreError.InitialState())
        )
        val result = Channel<ResultContract<EntityModelContract.MonolingualEntity, Exception>>()

        val id: EntityId = fixture.fixture()
        val language: LanguageTag = fixture.fixture()

        val remoteEntity = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = fixture.listFixture(),
        )

        val expected = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = fixture.listFixture(),
        )

        val localRepository = RepositoryStub()
        localRepository.fetchEntity = { _, _ -> null }

        var capturedEntity: EntityModelContract.MonolingualEntity? = null
        localRepository.updateEntity = { givenEntity ->
            capturedEntity = givenEntity

            expected
        }

        val remoteRepository = RepositoryStub()
        var capturedId: String? = null
        var capturedLanguage: String? = null
        remoteRepository.fetchEntity = { givenId, givenLanguage ->
            capturedId = givenId
            capturedLanguage = givenLanguage

            remoteEntity
        }

        val koin = koinApplication {
            modules(
                module {
                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.LOCAL)) {
                        localRepository
                    }

                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.REMOTE)) {
                        remoteRepository
                    }

                    single(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { testScope1 }
                    }

                    single {
                        flow
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<EntityModelContract.MonolingualEntity, Exception>> {
                        SharedFlowWrapperStub()
                    }
                }
            )
        }

        flow.onEach { item ->
            if (item.error !is EntityStoreError.InitialState) {
                result.send(item)
            }
        }.launchIn(testScope2)

        // When
        EntityStore(koin).fetchEntity(id, language)

        // Then
        runBlockingTestWithTimeout {
            result.receive().unwrap() mustBe expected

            capturedEntity mustBe remoteEntity

            capturedId mustBe id
            capturedLanguage mustBe language
        }
    }

    @Test
    fun `Given fetchEntity is called it mutates the Entity as Failure with the EntityStoreError, if the local and remote call are empty`() {
        // Given
        val flow = MutableStateFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>>(
            Failure(EntityStoreError.InitialState())
        )
        val result = Channel<ResultContract<EntityModelContract.MonolingualEntity, Exception>>()

        val id: EntityId = fixture.fixture()
        val language: LanguageTag = fixture.fixture()

        val localRepository = RepositoryStub()
        localRepository.fetchEntity = { _, _ -> null }

        val remoteRepository = RepositoryStub()
        remoteRepository.fetchEntity = { _, _ -> null }

        val koin = koinApplication {
            modules(
                module {
                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.LOCAL)) {
                        localRepository
                    }

                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.REMOTE)) {
                        remoteRepository
                    }

                    single(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { testScope1 }
                    }

                    single {
                        flow
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<EntityModelContract.MonolingualEntity, Exception>> {
                        SharedFlowWrapperStub()
                    }
                }
            )
        }

        flow.onEach { item ->
            if (item.error !is EntityStoreError.InitialState) {
                result.send(item)
            }
        }.launchIn(testScope2)

        // When
        EntityStore(koin).fetchEntity(id, language)

        // Then
        runBlockingTestWithTimeout {
            val error = assertFailsWith<EntityStoreError.MissingEntity> {
                result.receive().unwrap()
            }

            error.message mustBe "Entity ($id) in Language ($language) not found."
        }
    }

    @Test
    fun `Given fetchEntity is called it mutates the Entity as Failure with the EntityStoreError, if the local repository throws an error`() {
        // Given
        val expected = IllegalStateException()

        val flow = MutableStateFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>>(
            Failure(EntityStoreError.InitialState())
        )
        val result = Channel<ResultContract<EntityModelContract.MonolingualEntity, Exception>>()

        val id: EntityId = fixture.fixture()
        val language: LanguageTag = fixture.fixture()

        val localRepository = RepositoryStub()
        localRepository.fetchEntity = { _, _ -> throw expected }

        val koin = koinApplication {
            modules(
                module {
                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.LOCAL)) {
                        localRepository
                    }

                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.REMOTE)) {
                        RepositoryStub()
                    }

                    single(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { testScope1 }
                    }

                    single {
                        flow
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<EntityModelContract.MonolingualEntity, Exception>> {
                        SharedFlowWrapperStub()
                    }
                }
            )
        }

        flow.onEach { item ->
            if (item.error !is EntityStoreError.InitialState) {
                result.send(item)
            }
        }.launchIn(testScope2)

        // When
        EntityStore(koin).fetchEntity(id, language)

        // Then
        runBlockingTestWithTimeout {
            val error = assertFails {
                result.receive().unwrap()
            }

            error sameAs expected
        }
    }

    @Test
    fun `Given fetchEntity is called it mutates the Entity as Failure with the EntityStoreError, if the remote repository throws an error`() {
        // Given
        val expected = IllegalStateException()

        val flow = MutableStateFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>>(
            Failure(EntityStoreError.InitialState())
        )
        val result = Channel<ResultContract<EntityModelContract.MonolingualEntity, Exception>>()

        val id: EntityId = fixture.fixture()
        val language: LanguageTag = fixture.fixture()

        val localRepository = RepositoryStub()
        localRepository.fetchEntity = { _, _ -> null }

        val remoteRepository = RepositoryStub()
        remoteRepository.fetchEntity = { _, _ -> throw expected }

        val koin = koinApplication {
            modules(
                module {
                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.LOCAL)) {
                        localRepository
                    }

                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.REMOTE)) {
                        remoteRepository
                    }

                    single(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { testScope1 }
                    }

                    single {
                        flow
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<EntityModelContract.MonolingualEntity, Exception>> {
                        SharedFlowWrapperStub()
                    }
                }
            )
        }

        flow.onEach { item ->
            if (item.error !is EntityStoreError.InitialState) {
                result.send(item)
            }
        }.launchIn(testScope2)

        // When
        EntityStore(koin).fetchEntity(id, language)

        // Then
        runBlockingTestWithTimeout {
            val error = assertFails {
                result.receive().unwrap()
            }

            error sameAs expected
        }
    }

    @Test
    fun `Given setLabel is called with a String it emits a Failure, if the latest State is an failure`() {
        // Given
        val flow = MutableStateFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>>(
            Failure(EntityStoreError.InitialState())
        )
        val result = Channel<ResultContract<EntityModelContract.MonolingualEntity, Exception>>()

        val koin = koinApplication {
            modules(
                module {
                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.LOCAL)) {
                        RepositoryStub()
                    }

                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.REMOTE)) {
                        RepositoryStub()
                    }

                    single(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { testScope1 }
                    }

                    single {
                        flow
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<EntityModelContract.MonolingualEntity, Exception>> {
                        SharedFlowWrapperStub()
                    }
                }
            )
        }

        flow.onEach { item ->
            if (item.error !is EntityStoreError.InitialState) {
                result.send(item)
            }
        }.launchIn(testScope2)

        // When
        EntityStore(koin).setLabel(fixture.fixture())

        // Then
        runBlockingTestWithTimeout {
            val error = assertFailsWith<EntityStoreError.MutationFailure> {
                result.receive().unwrap()
            }

            error.message sameAs "Cannot mutate Entity, since last event resulted in an error."
        }
    }

    @Test
    fun `Given setLabel is called it mutates the Label with the given String, if the latest State is an Success`() {
        // Given
        val newLabel: String = fixture.fixture()

        val entity = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = fixture.listFixture(),
        )

        val flow = MutableStateFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>>(
            Success(entity)
        )
        val result = Channel<ResultContract<EntityModelContract.MonolingualEntity, Exception>>()

        val koin = koinApplication {
            modules(
                module {
                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.LOCAL)) {
                        RepositoryStub()
                    }

                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.REMOTE)) {
                        RepositoryStub()
                    }

                    single(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { testScope1 }
                    }

                    single {
                        flow
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<EntityModelContract.MonolingualEntity, Exception>> {
                        SharedFlowWrapperStub()
                    }
                }
            )
        }

        flow.onEach { item ->
            if (item.value!!.label != entity.label) {
                result.send(item)
            }
        }.launchIn(testScope2)

        // When
        EntityStore(koin).setLabel(newLabel)

        // Then
        runBlockingTestWithTimeout {
            result.receive().unwrap() mustBe entity.copy(label = newLabel)
        }
    }

    @Test
    fun `Given setDescription is called with a String it emits a Failure, if the latest State is an failure`() {
        // Given
        val flow = MutableStateFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>>(
            Failure(EntityStoreError.InitialState())
        )
        val result = Channel<ResultContract<EntityModelContract.MonolingualEntity, Exception>>()

        val koin = koinApplication {
            modules(
                module {
                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.LOCAL)) {
                        RepositoryStub()
                    }

                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.REMOTE)) {
                        RepositoryStub()
                    }

                    single(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { testScope1 }
                    }

                    single {
                        flow
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<EntityModelContract.MonolingualEntity, Exception>> {
                        SharedFlowWrapperStub()
                    }
                }
            )
        }

        flow.onEach { item ->
            if (item.error !is EntityStoreError.InitialState) {
                result.send(item)
            }
        }.launchIn(testScope2)

        // When
        EntityStore(koin).setDescription(fixture.fixture())

        // Then
        runBlockingTestWithTimeout {
            val error = assertFailsWith<EntityStoreError.MutationFailure> {
                result.receive().unwrap()
            }

            error.message sameAs "Cannot mutate Entity, since last event resulted in an error."
        }
    }

    @Test
    fun `Given setDescritpion is called it mutates the Description with the given String, if the latest State is an Success`() {
        // Given
        val newDescription: String = fixture.fixture()

        val entity = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = fixture.listFixture(),
        )

        val flow = MutableStateFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>>(
            Success(entity)
        )
        val result = Channel<ResultContract<EntityModelContract.MonolingualEntity, Exception>>()

        val koin = koinApplication {
            modules(
                module {
                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.LOCAL)) {
                        RepositoryStub()
                    }

                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.REMOTE)) {
                        RepositoryStub()
                    }

                    single(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { testScope1 }
                    }

                    single {
                        flow
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<EntityModelContract.MonolingualEntity, Exception>> {
                        SharedFlowWrapperStub()
                    }
                }
            )
        }

        flow.onEach { item ->
            if (item.value!!.description != entity.description) {
                result.send(item)
            }
        }.launchIn(testScope2)

        // When
        EntityStore(koin).setDescription(newDescription)

        // Then
        runBlockingTestWithTimeout {
            result.receive().unwrap() mustBe entity.copy(description = newDescription)
        }
    }

    @Test
    fun `Given setAliases is called with a String it emits a Failure, if the latest State is an failure`() {
        // Given
        val flow = MutableStateFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>>(
            Failure(EntityStoreError.InitialState())
        )
        val result = Channel<ResultContract<EntityModelContract.MonolingualEntity, Exception>>()

        val koin = koinApplication {
            modules(
                module {
                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.LOCAL)) {
                        RepositoryStub()
                    }

                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.REMOTE)) {
                        RepositoryStub()
                    }

                    single(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { testScope1 }
                    }

                    single {
                        flow
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<EntityModelContract.MonolingualEntity, Exception>> {
                        SharedFlowWrapperStub()
                    }
                }
            )
        }

        flow.onEach { item ->
            if (item.error !is EntityStoreError.InitialState) {
                result.send(item)
            }
        }.launchIn(testScope2)

        // When
        EntityStore(koin).setAliases(fixture.listFixture())

        // Then
        runBlockingTestWithTimeout {
            val error = assertFailsWith<EntityStoreError.MutationFailure> {
                result.receive().unwrap()
            }

            error.message sameAs "Cannot mutate Entity, since last event resulted in an error."
        }
    }

    @Test
    fun `Given setAliases is called it mutates the Aliases with the given List, if the latest State is an Success`() {
        // Given
        val newAliases: List<String> = fixture.listFixture()

        val entity = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = fixture.listFixture(),
        )

        val flow = MutableStateFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>>(
            Success(entity)
        )
        val result = Channel<ResultContract<EntityModelContract.MonolingualEntity, Exception>>()

        val koin = koinApplication {
            modules(
                module {
                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.LOCAL)) {
                        RepositoryStub()
                    }

                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.REMOTE)) {
                        RepositoryStub()
                    }

                    single(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { testScope1 }
                    }

                    single {
                        flow
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<EntityModelContract.MonolingualEntity, Exception>> {
                        SharedFlowWrapperStub()
                    }
                }
            )
        }

        flow.onEach { item ->
            if (item.value!!.aliases != entity.aliases) {
                result.send(item)
            }
        }.launchIn(testScope2)

        // When
        EntityStore(koin).setAliases(newAliases)

        // Then
        runBlockingTestWithTimeout {
            result.receive().unwrap() mustBe entity.copy(aliases = newAliases)
        }
    }

    @Test
    fun `Given create is called with a LanguageTag and a EntityType it creates a new Entity with the given Parameter`() {
        // Given
        val language: LanguageTag = fixture.fixture()
        val type = EntityModelContract.EntityType.PROPERTY

        val flow = MutableStateFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>>(
            Failure(EntityStoreError.InitialState())
        )
        val result = Channel<ResultContract<EntityModelContract.MonolingualEntity, Exception>>()

        val koin = koinApplication {
            modules(
                module {
                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.LOCAL)) {
                        RepositoryStub()
                    }

                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.REMOTE)) {
                        RepositoryStub()
                    }

                    single(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { testScope1 }
                    }

                    single {
                        flow
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<EntityModelContract.MonolingualEntity, Exception>> {
                        SharedFlowWrapperStub()
                    }
                }
            )
        }

        flow.onEach { item ->
            if (item.error !is EntityStoreError.InitialState) {
                result.send(item)
            }
        }.launchIn(testScope2)

        // When
        EntityStore(koin).create(language, type)

        // Then
        runBlockingTestWithTimeout {
            result.receive().unwrap() mustBe MonolingualEntity(
                id = "",
                type = type,
                revision = 0,
                language = language,
                lastModification = Instant.DISTANT_PAST,
                isEditable = true,
                label = null,
                description = null,
                aliases = emptyList(),
            )
        }
    }

    @Test
    fun `Given rollback is called, after create, it goes into enitial state`() {
        // Given
        val inMemoryEntity = MonolingualEntity(
            id = "",
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = fixture.listFixture(),
        )

        val flow = MutableStateFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>>(
            Success(inMemoryEntity)
        )
        val result = Channel<ResultContract<EntityModelContract.MonolingualEntity, Exception>>()

        val koin = koinApplication {
            modules(
                module {
                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.LOCAL)) {
                        RepositoryStub()
                    }

                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.REMOTE)) {
                        RepositoryStub()
                    }

                    single(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { testScope1 }
                    }

                    single {
                        flow
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<EntityModelContract.MonolingualEntity, Exception>> {
                        SharedFlowWrapperStub()
                    }
                }
            )
        }

        flow.onEach { item ->
            if (item.value != inMemoryEntity) {
                result.send(item)
            }
        }.launchIn(testScope2)

        // When
        EntityStore(koin).rollback()

        // Then
        runBlockingTest {
            withTimeout(2000) {
                assertFailsWith<EntityStoreError.InvalidRollbackState> {
                    result.receive().unwrap()
                }
            }
        }
    }

    @Test
    fun `Given rollback is called, while in Error state, it emits a Failure with InvalidState`() {
        // Given
        val expected = RuntimeException()

        val flow = MutableStateFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>>(
            Failure(expected)
        )

        val result = Channel<ResultContract<EntityModelContract.MonolingualEntity, Exception>>()

        val koin = koinApplication {
            modules(
                module {
                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.LOCAL)) {
                        RepositoryStub()
                    }

                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.REMOTE)) {
                        RepositoryStub()
                    }

                    single(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { testScope1 }
                    }

                    single {
                        flow
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<EntityModelContract.MonolingualEntity, Exception>> {
                        SharedFlowWrapperStub()
                    }
                }
            )
        }

        flow.onEach { item -> result.send(item) }.launchIn(testScope2)

        // When
        EntityStore(koin).rollback()

        // Then
        runBlockingTest {
            withTimeout(2000) {
                assertFails {
                    result.receive().unwrap()
                }

                assertFailsWith<EntityStoreError.InvalidCreationState> {
                    result.receive().unwrap()
                }
            }
        }
    }

    @Test
    fun `Given rollback is called, while in Success state, it rollsback to stored Entity`() {
        // Given
        val id: EntityId = fixture.fixture()
        val language: LanguageTag = fixture.fixture()

        val expected = MonolingualEntity(
            id = id,
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = language,
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = fixture.listFixture(),
        )

        val inMemoryEntity = MonolingualEntity(
            id = id,
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = language,
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = fixture.listFixture(),
        )

        val flow = MutableStateFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>>(
            Success(inMemoryEntity)
        )

        val result = Channel<ResultContract<EntityModelContract.MonolingualEntity, Exception>>()

        val localRepository = RepositoryStub()

        var capturedId: EntityId? = null
        var capturedLanguage: LanguageTag? = null
        localRepository.fetchEntity = { givenId, givenLanguage ->
            capturedId = givenId
            capturedLanguage = givenLanguage

            expected
        }

        val koin = koinApplication {
            modules(
                module {
                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.LOCAL)) {
                        localRepository
                    }

                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.REMOTE)) {
                        RepositoryStub()
                    }

                    single(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { testScope1 }
                    }

                    single {
                        flow
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<EntityModelContract.MonolingualEntity, Exception>> {
                        SharedFlowWrapperStub()
                    }
                }
            )
        }

        flow.onEach { item ->
            if (item.value != inMemoryEntity) {
                result.send(item)
            }
        }.launchIn(testScope2)

        // When
        EntityStore(koin).rollback()

        // Then
        runBlockingTest {
            withTimeout(2000) {
                result.receive().unwrap() sameAs expected

                capturedId mustBe id
                capturedLanguage mustBe language
            }
        }
    }

    @Test
    fun `Given rollback is called, while in Success state, it emits a Failure with MissingEntity if the LocalRepository returns null`() {
        // Given
        val id: EntityId = fixture.fixture()
        val language: LanguageTag = fixture.fixture()

        val inMemoryEntity = MonolingualEntity(
            id = id,
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = language,
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = fixture.listFixture(),
        )

        val flow = MutableStateFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>>(
            Success(inMemoryEntity)
        )

        val result = Channel<ResultContract<EntityModelContract.MonolingualEntity, Exception>>()

        val localRepository = RepositoryStub()

        localRepository.fetchEntity = { _, _ -> null }

        val koin = koinApplication {
            modules(
                module {
                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.LOCAL)) {
                        localRepository
                    }

                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.REMOTE)) {
                        RepositoryStub()
                    }

                    single(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { testScope1 }
                    }

                    single {
                        flow
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<EntityModelContract.MonolingualEntity, Exception>> {
                        SharedFlowWrapperStub()
                    }
                }
            )
        }

        flow.onEach { item ->
            if (item.value != inMemoryEntity) {
                result.send(item)
            }
        }.launchIn(testScope2)

        // When
        EntityStore(koin).rollback()

        // Then
        runBlockingTest {
            withTimeout(2000) {
                val error = assertFailsWith<EntityStoreError.MissingEntity> {
                    result.receive().unwrap()
                }

                error.message mustBe "Entity ($id) in Language ($language) not found."
            }
        }
    }

    @Test
    fun `Given rollback is called, while in Success state, it emtis a Failure with any Exceptions from the LocalRepository`() {
        // Given
        val id: EntityId = fixture.fixture()
        val language: LanguageTag = fixture.fixture()

        val expected = IllegalStateException()

        val inMemoryEntity = MonolingualEntity(
            id = id,
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = language,
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = fixture.listFixture(),
        )

        val flow = MutableStateFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>>(
            Success(inMemoryEntity)
        )

        val result = Channel<ResultContract<EntityModelContract.MonolingualEntity, Exception>>()

        val localRepository = RepositoryStub()

        localRepository.fetchEntity = { _, _ -> throw expected }

        val koin = koinApplication {
            modules(
                module {
                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.LOCAL)) {
                        localRepository
                    }

                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.REMOTE)) {
                        RepositoryStub()
                    }

                    single(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { testScope1 }
                    }

                    single {
                        flow
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<EntityModelContract.MonolingualEntity, Exception>> {
                        SharedFlowWrapperStub()
                    }
                }
            )
        }

        flow.onEach { item ->
            if (item.value != inMemoryEntity) {
                result.send(item)
            }
        }.launchIn(testScope2)

        // When
        EntityStore(koin).rollback()

        // Then
        runBlockingTest {
            withTimeout(2000) {
                val error = assertFails {
                    result.receive().unwrap()
                }

                error sameAs expected
            }
        }
    }

    @Test
    fun `Given reset is called it goes back into the inital state`() {
        // Given
        val entity = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = fixture.listFixture(),
        )

        val flow = MutableStateFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>>(
            Success(entity)
        )
        val result = Channel<ResultContract<EntityModelContract.MonolingualEntity, Exception>>()

        val koin = koinApplication {
            modules(
                module {
                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.LOCAL)) {
                        RepositoryStub()
                    }

                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.REMOTE)) {
                        RepositoryStub()
                    }

                    single(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { testScope1 }
                    }

                    single {
                        flow
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<EntityModelContract.MonolingualEntity, Exception>> {
                        SharedFlowWrapperStub()
                    }
                }
            )
        }

        flow.onEach { item ->
            if (item.value != entity) {
                result.send(item)
            }
        }.launchIn(testScope2)

        // When
        EntityStore(koin).reset()

        // Then
        runBlockingTest {
            withTimeout(2000) {
                assertFailsWith<EntityStoreError.InitialState> {
                    result.receive().unwrap()
                }
            }
        }
    }

    @Test
    fun `Given save is called after an ErrorState, it emits a Failure with InvalidState`() {
        // Given
        val expected = RuntimeException()

        val flow = MutableStateFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>>(
            Failure(expected)
        )

        val result = Channel<ResultContract<EntityModelContract.MonolingualEntity, Exception>>()

        val koin = koinApplication {
            modules(
                module {
                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.LOCAL)) {
                        RepositoryStub()
                    }

                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.REMOTE)) {
                        RepositoryStub()
                    }

                    single(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { testScope1 }
                    }

                    single {
                        flow
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<EntityModelContract.MonolingualEntity, Exception>> {
                        SharedFlowWrapperStub()
                    }
                }
            )
        }

        flow.onEach { item -> result.send(item) }.launchIn(testScope2)

        // When
        EntityStore(koin).save()

        // Then
        runBlockingTest {
            withTimeout(2000) {
                assertFails {
                    result.receive().unwrap()
                }

                assertFailsWith<EntityStoreError.InvalidCreationState> {
                    result.receive().unwrap()
                }
            }
        }
    }

    @Test
    fun `Given save is called after an EntityCreation, it creates a new Entity remotely and locally`() {
        // Given
        val inMemoryEntity = MonolingualEntity(
            id = "",
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.DISTANT_PAST,
            isEditable = fixture.fixture(),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = fixture.listFixture(),
        )

        val expected = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = fixture.listFixture(),
        )

        val flow = MutableStateFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>>(
            Success(inMemoryEntity)
        )

        val result = Channel<ResultContract<EntityModelContract.MonolingualEntity, Exception>>()

        val remoteRepository = RepositoryStub()
        var capturedRemote: EntityModelContract.MonolingualEntity? = null
        remoteRepository.createEntity = { givenEntity ->
            capturedRemote = givenEntity

            expected
        }

        val localRepository = RepositoryStub()
        var capturedLocal: EntityModelContract.MonolingualEntity? = null
        localRepository.createEntity = { givenEntity ->
            capturedLocal = givenEntity

            expected
        }

        val koin = koinApplication {
            modules(
                module {
                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.LOCAL)) {
                        localRepository
                    }

                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.REMOTE)) {
                        remoteRepository
                    }

                    single(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { testScope1 }
                    }

                    single {
                        flow
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<EntityModelContract.MonolingualEntity, Exception>> {
                        SharedFlowWrapperStub()
                    }
                }
            )
        }

        flow.onEach { item ->
            if (item.value != inMemoryEntity) {
                result.send(item)
            }
        }.launchIn(testScope2)

        // When
        EntityStore(koin).save()

        // Then
        runBlockingTest {
            withTimeout(2000) {
                result.receive().unwrap() sameAs expected

                capturedRemote mustBe inMemoryEntity
                capturedLocal mustBe expected
            }
        }
    }

    @Test
    fun `Given save is called after an EntityCreation, it emits a Failure wiht any Error from the remoteRepository`() {
        // Given
        val inMemoryEntity = MonolingualEntity(
            id = "",
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.DISTANT_PAST,
            isEditable = fixture.fixture(),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = fixture.listFixture(),
        )

        val entity = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = fixture.listFixture(),
        )

        val expected = IllegalArgumentException()

        val flow = MutableStateFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>>(
            Success(inMemoryEntity)
        )

        val result = Channel<ResultContract<EntityModelContract.MonolingualEntity, Exception>>()

        val remoteRepository = RepositoryStub()
        remoteRepository.createEntity = { throw expected }

        val localRepository = RepositoryStub()
        localRepository.createEntity = { entity }

        val koin = koinApplication {
            modules(
                module {
                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.LOCAL)) {
                        localRepository
                    }

                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.REMOTE)) {
                        remoteRepository
                    }

                    single(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { testScope1 }
                    }

                    single {
                        flow
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<EntityModelContract.MonolingualEntity, Exception>> {
                        SharedFlowWrapperStub()
                    }
                }
            )
        }

        flow.onEach { item ->
            if (item.value != inMemoryEntity) {
                result.send(item)
            }
        }.launchIn(testScope2)

        // When
        EntityStore(koin).save()

        // Then
        runBlockingTest {
            withTimeout(2000) {
                val error = assertFails {
                    result.receive().unwrap()
                }

                error sameAs expected
            }
        }
    }

    @Test
    fun `Given save is called after an EntityCreation, it emits a Failure wiht any Error from the localRepository`() {
        // Given
        val inMemoryEntity = MonolingualEntity(
            id = "",
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.DISTANT_PAST,
            isEditable = fixture.fixture(),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = fixture.listFixture(),
        )

        val entity = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = fixture.listFixture(),
        )

        val expected = IllegalArgumentException()

        val flow = MutableStateFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>>(
            Success(inMemoryEntity)
        )

        val result = Channel<ResultContract<EntityModelContract.MonolingualEntity, Exception>>()

        val remoteRepository = RepositoryStub()
        remoteRepository.createEntity = { entity }

        val localRepository = RepositoryStub()
        localRepository.createEntity = { throw expected }

        val koin = koinApplication {
            modules(
                module {
                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.LOCAL)) {
                        localRepository
                    }

                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.REMOTE)) {
                        remoteRepository
                    }

                    single(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { testScope1 }
                    }

                    single {
                        flow
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<EntityModelContract.MonolingualEntity, Exception>> {
                        SharedFlowWrapperStub()
                    }
                }
            )
        }

        flow.onEach { item ->
            if (item.value != inMemoryEntity) {
                result.send(item)
            }
        }.launchIn(testScope2)

        // When
        EntityStore(koin).save()

        // Then
        runBlockingTest {
            withTimeout(2000) {
                val error = assertFails {
                    result.receive().unwrap()
                }

                error sameAs expected
            }
        }
    }

    @Test
    fun `Given save is called after an EntityCreation, it emits a Failure due to the remote Repository returns null`() {
        // Given
        val inMemoryEntity = MonolingualEntity(
            id = "",
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.DISTANT_PAST,
            isEditable = fixture.fixture(),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = fixture.listFixture(),
        )

        val entity = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = fixture.listFixture(),
        )

        val flow = MutableStateFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>>(
            Success(inMemoryEntity)
        )

        val result = Channel<ResultContract<EntityModelContract.MonolingualEntity, Exception>>()

        val remoteRepository = RepositoryStub()
        remoteRepository.createEntity = { null }

        val localRepository = RepositoryStub()
        localRepository.createEntity = { entity }

        val koin = koinApplication {
            modules(
                module {
                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.LOCAL)) {
                        localRepository
                    }

                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.REMOTE)) {
                        remoteRepository
                    }

                    single(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { testScope1 }
                    }

                    single {
                        flow
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<EntityModelContract.MonolingualEntity, Exception>> {
                        SharedFlowWrapperStub()
                    }
                }
            )
        }

        flow.onEach { item ->
            if (item.value != inMemoryEntity) {
                result.send(item)
            }
        }.launchIn(testScope2)

        // When
        EntityStore(koin).save()

        // Then
        runBlockingTest {
            withTimeout(2000) {
                val error = assertFailsWith<EntityStoreError.CreationRemoteFailure> {
                    result.receive().unwrap()
                }

                error.message mustBe "Cannot create Entity in Language (${inMemoryEntity.language})"
            }
        }
    }

    @Test
    fun `Given save is called after an EntityCreation, it emits a Failure due to the local Repository returns null`() {
        // Given
        val inMemoryEntity = MonolingualEntity(
            id = "",
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.DISTANT_PAST,
            isEditable = fixture.fixture(),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = fixture.listFixture(),
        )

        val entity = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = fixture.listFixture(),
        )

        val flow = MutableStateFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>>(
            Success(inMemoryEntity)
        )

        val result = Channel<ResultContract<EntityModelContract.MonolingualEntity, Exception>>()

        val remoteRepository = RepositoryStub()
        remoteRepository.createEntity = { entity }

        val localRepository = RepositoryStub()
        localRepository.createEntity = { null }

        val koin = koinApplication {
            modules(
                module {
                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.LOCAL)) {
                        localRepository
                    }

                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.REMOTE)) {
                        remoteRepository
                    }

                    single(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { testScope1 }
                    }

                    single {
                        flow
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<EntityModelContract.MonolingualEntity, Exception>> {
                        SharedFlowWrapperStub()
                    }
                }
            )
        }

        flow.onEach { item ->
            if (item.value != inMemoryEntity) {
                result.send(item)
            }
        }.launchIn(testScope2)

        // When
        EntityStore(koin).save()

        // Then
        runBlockingTest {
            withTimeout(2000) {
                val error = assertFailsWith<EntityStoreError.CreationLocalFailure> {
                    result.receive().unwrap()
                }

                error.message mustBe "Cannot store created Entity (${entity.id}) in Language (${entity.language})"
            }
        }
    }

    // Update
    @Test
    fun `Given save is called after an EntityUpdate, it creates a new Entity remotely and locally`() {
        // Given
        val inMemoryEntity = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.DISTANT_PAST,
            isEditable = fixture.fixture(),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = fixture.listFixture(),
        )

        val expected = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = fixture.listFixture(),
        )

        val flow = MutableStateFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>>(
            Success(inMemoryEntity)
        )

        val result = Channel<ResultContract<EntityModelContract.MonolingualEntity, Exception>>()

        val remoteRepository = RepositoryStub()
        var capturedRemote: EntityModelContract.MonolingualEntity? = null
        remoteRepository.updateEntity = { givenEntity ->
            capturedRemote = givenEntity

            expected
        }

        val localRepository = RepositoryStub()
        var capturedLocal: EntityModelContract.MonolingualEntity? = null
        localRepository.updateEntity = { givenEntity ->
            capturedLocal = givenEntity

            expected
        }

        val koin = koinApplication {
            modules(
                module {
                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.LOCAL)) {
                        localRepository
                    }

                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.REMOTE)) {
                        remoteRepository
                    }

                    single(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { testScope1 }
                    }

                    single {
                        flow
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<EntityModelContract.MonolingualEntity, Exception>> {
                        SharedFlowWrapperStub()
                    }
                }
            )
        }

        flow.onEach { item ->
            if (item.value != inMemoryEntity) {
                result.send(item)
            }
        }.launchIn(testScope2)

        // When
        EntityStore(koin).save()

        // Then
        runBlockingTest {
            withTimeout(2000) {
                result.receive().unwrap() sameAs expected

                capturedRemote mustBe inMemoryEntity
                capturedLocal mustBe expected
            }
        }
    }

    @Test
    fun `Given save is called after an EntityUpdate, it emits Failure with any Error from the RemoteRepository`() {
        // Given
        val inMemoryEntity = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.DISTANT_PAST,
            isEditable = fixture.fixture(),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = fixture.listFixture(),
        )

        val entity = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = fixture.listFixture(),
        )

        val expected = IllegalArgumentException()

        val flow = MutableStateFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>>(
            Success(inMemoryEntity)
        )

        val result = Channel<ResultContract<EntityModelContract.MonolingualEntity, Exception>>()

        val remoteRepository = RepositoryStub()
        remoteRepository.updateEntity = { throw expected }

        val localRepository = RepositoryStub()
        localRepository.updateEntity = { entity }

        val koin = koinApplication {
            modules(
                module {
                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.LOCAL)) {
                        localRepository
                    }

                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.REMOTE)) {
                        remoteRepository
                    }

                    single(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { testScope1 }
                    }

                    single {
                        flow
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<EntityModelContract.MonolingualEntity, Exception>> {
                        SharedFlowWrapperStub()
                    }
                }
            )
        }

        flow.onEach { item ->
            if (item.value != inMemoryEntity) {
                result.send(item)
            }
        }.launchIn(testScope2)

        // When
        EntityStore(koin).save()

        // Then
        runBlockingTest {
            withTimeout(2000) {
                val error = assertFails {
                    result.receive().unwrap()
                }

                error sameAs expected
            }
        }
    }

    @Test
    fun `Given save is called after an EntityUpdate, it emits a Failure with any Error from the LocalRepository`() {
        // Given
        val inMemoryEntity = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.DISTANT_PAST,
            isEditable = fixture.fixture(),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = fixture.listFixture(),
        )

        val entity = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = fixture.listFixture(),
        )

        val expected = IllegalArgumentException()

        val flow = MutableStateFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>>(
            Success(inMemoryEntity)
        )

        val result = Channel<ResultContract<EntityModelContract.MonolingualEntity, Exception>>()

        val remoteRepository = RepositoryStub()
        remoteRepository.updateEntity = { entity }

        val localRepository = RepositoryStub()
        localRepository.updateEntity = { throw expected }

        val koin = koinApplication {
            modules(
                module {
                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.LOCAL)) {
                        localRepository
                    }

                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.REMOTE)) {
                        remoteRepository
                    }

                    single(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { testScope1 }
                    }

                    single {
                        flow
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<EntityModelContract.MonolingualEntity, Exception>> {
                        SharedFlowWrapperStub()
                    }
                }
            )
        }

        flow.onEach { item ->
            if (item.value != inMemoryEntity) {
                result.send(item)
            }
        }.launchIn(testScope2)

        // When
        EntityStore(koin).save()

        // Then
        runBlockingTest {
            withTimeout(2000) {
                val error = assertFails {
                    result.receive().unwrap()
                }

                error sameAs expected
            }
        }
    }

    @Test
    fun `Given save is called after an EntityUpdate, it emits a Failure due to the remote Repository returns null`() {
        // Given
        val inMemoryEntity = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.DISTANT_PAST,
            isEditable = fixture.fixture(),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = fixture.listFixture(),
        )

        val entity = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = fixture.listFixture(),
        )

        val flow = MutableStateFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>>(
            Success(inMemoryEntity)
        )

        val result = Channel<ResultContract<EntityModelContract.MonolingualEntity, Exception>>()

        val remoteRepository = RepositoryStub()
        remoteRepository.updateEntity = { null }

        val localRepository = RepositoryStub()
        localRepository.updateEntity = { entity }

        val koin = koinApplication {
            modules(
                module {
                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.LOCAL)) {
                        localRepository
                    }

                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.REMOTE)) {
                        remoteRepository
                    }

                    single(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { testScope1 }
                    }

                    single {
                        flow
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<EntityModelContract.MonolingualEntity, Exception>> {
                        SharedFlowWrapperStub()
                    }
                }
            )
        }

        flow.onEach { item ->
            if (item.value != inMemoryEntity) {
                result.send(item)
            }
        }.launchIn(testScope2)

        // When
        EntityStore(koin).save()

        // Then
        runBlockingTest {
            withTimeout(2000) {
                val error = assertFailsWith<EntityStoreError.UpdateRemoteFailure> {
                    result.receive().unwrap()
                }

                error.message mustBe "Cannot edit Entity (${inMemoryEntity.id}) in Language (${inMemoryEntity.language})"
            }
        }
    }

    @Test
    fun `Given save is called after an EntityUpdate, it emits a Failure due to the local Repository returns null`() {
        // Given
        val inMemoryEntity = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.DISTANT_PAST,
            isEditable = fixture.fixture(),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = fixture.listFixture(),
        )

        val entity = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = fixture.fixture(),
            description = fixture.fixture(),
            aliases = fixture.listFixture(),
        )

        val flow = MutableStateFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>>(
            Success(inMemoryEntity)
        )

        val result = Channel<ResultContract<EntityModelContract.MonolingualEntity, Exception>>()

        val remoteRepository = RepositoryStub()
        remoteRepository.updateEntity = { entity }

        val localRepository = RepositoryStub()
        localRepository.updateEntity = { null }

        val koin = koinApplication {
            modules(
                module {
                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.LOCAL)) {
                        localRepository
                    }

                    single<DomainContract.Repository>(named(DomainContract.DomainKoinIds.REMOTE)) {
                        remoteRepository
                    }

                    single(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { testScope1 }
                    }

                    single {
                        flow
                    }

                    single<CoroutineWrapperContract.SharedFlowWrapper<EntityModelContract.MonolingualEntity, Exception>> {
                        SharedFlowWrapperStub()
                    }
                }
            )
        }

        flow.onEach { item ->
            if (item.value != inMemoryEntity) {
                result.send(item)
            }
        }.launchIn(testScope2)

        // When
        EntityStore(koin).save()

        // Then
        runBlockingTest {
            withTimeout(2000) {
                val error = assertFailsWith<EntityStoreError.UpdateLocalFailure> {
                    result.receive().unwrap()
                }

                error.message mustBe "Cannot store edited Entity (${entity.id}) in Language (${entity.language})"
            }
        }
    }
}
