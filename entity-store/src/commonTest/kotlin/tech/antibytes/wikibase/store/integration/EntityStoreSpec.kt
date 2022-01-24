/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.integration

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withTimeout
import kotlinx.datetime.Instant
import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.util.coroutine.result.ResultContract
import tech.antibytes.util.test.annotations.RobolectricConfig
import tech.antibytes.util.test.annotations.RobolectricTestRunner
import tech.antibytes.util.test.annotations.RunWithRobolectricTestRunner
import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.coroutine.runBlockingTestWithTimeout
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.wikibase.store.database.entity.WikibaseDataBase
import tech.antibytes.wikibase.store.entity.EntityStoreContract
import tech.antibytes.wikibase.store.entity.database.DatabaseDriver
import tech.antibytes.wikibase.store.entity.domain.EntityStore
import tech.antibytes.wikibase.store.entity.domain.model.EntityId
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract
import tech.antibytes.wikibase.store.entity.domain.model.MonolingualEntity
import tech.antibytes.wikibase.store.entity.lang.EntityStoreError
import tech.antibytes.wikibase.store.entity.testScope1
import tech.antibytes.wikibase.store.entity.testScope2
import tech.antibytes.wikibase.store.entity.transfer.dto.LanguageValuePair
import tech.antibytes.wikibase.store.entity.transfer.dto.RevisionedEntity
import tech.antibytes.wikibase.store.mock.MwClientStub
import tech.antibytes.wikibase.store.mock.SuspendingFunctionWrapperStub
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@RobolectricConfig(manifest = "--none")
@RunWithRobolectricTestRunner(RobolectricTestRunner::class)
class EntityStoreSpec {
    private val fixture = kotlinFixture()
    private val db = DatabaseDriver()
    private val client = MwClientStub()

    private lateinit var entityStore: EntityStoreContract.EntityStore

    @BeforeTest
    fun setUp() {
        db.open(WikibaseDataBase.Schema)

        entityStore = EntityStore.getInstance(
            client,
            db.dataBase.entityQueries,
            { testScope1 },
            { testScope2 }
        )
    }

    @AfterTest
    fun tearDown() {
        db.close()
    }

    @Test
    fun `It fetches a Entities`() {
        // Given
        val actual = Channel<ResultContract<EntityModelContract.MonolingualEntity, Exception>>()

        val id1: EntityId = fixture.fixture()
        val language1: EntityId = fixture.fixture()

        val expected = MonolingualEntity(
            id = id1,
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = language1,
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = true,
            label = fixture.fixture<String>(),
            description = fixture.fixture<String>(),
            aliases = listOf(fixture.fixture(), fixture.fixture()),
        )

        val remoteEntity1 = RevisionedEntity(
            id = id1,
            type = DataModelContract.EntityType.ITEM,
            revision = expected.revision,
            lastModification = expected.lastModification,
            labels = mapOf(
                language1 to LanguageValuePair(language1, expected.label!!)
            ),
            descriptions = mapOf(
                language1 to LanguageValuePair(language1, expected.description!!)
            ),
            aliases = mapOf(
                language1 to listOf(
                    LanguageValuePair(language1, expected.aliases.first()),
                    LanguageValuePair(language1, expected.aliases.last())
                )
            )
        )

        val id2: String = fixture.fixture()
        val language2: String = fixture.fixture()
        val remoteEntity2 = RevisionedEntity(
            id = id2,
            type = DataModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            lastModification = expected.lastModification,
            labels = mapOf(
                language2 to LanguageValuePair(language2, fixture.fixture())
            ),
            descriptions = mapOf(
                language2 to LanguageValuePair(language2, fixture.fixture())
            ),
            aliases = mapOf(
                language2 to listOf(
                    LanguageValuePair(language2, fixture.fixture()),
                    LanguageValuePair(language2, fixture.fixture())
                )
            )
        )

        val remoteEntities = mutableListOf(
            listOf(remoteEntity1),
            listOf(remoteEntity2)
        )

        client.wikibase.fetchEntities = { _, _ ->
            SuspendingFunctionWrapperStub { remoteEntities.removeFirst() }
        }

        client.page.fetchRestrictions = {
            SuspendingFunctionWrapperStub { emptyList() }
        }

        // When
        entityStore.entity.subscribeWithSuspendingFunction { result ->
            actual.send(result)
        }

        // Then
        runBlockingTestWithTimeout {
            actual.receive().error!! fulfils EntityStoreError.InitialState::class
        }

        // When
        entityStore.fetchEntity(id1, language1)

        // Then
        runBlockingTestWithTimeout {
            actual.receive().unwrap() mustBe expected
        }

        // When
        entityStore.fetchEntity(id2, language2)

        // Then
        runBlockingTest {
            withTimeout(2000) {
                actual.receive().isSuccess() mustBe true
            }
        }

        // When
        entityStore.fetchEntity(id1, language1)

        // Then
        runBlockingTestWithTimeout {
            actual.receive().unwrap() mustBe expected
        }
    }

    @Test
    fun `It creates Entities`() {
        // Given
        val actual = Channel<ResultContract<EntityModelContract.MonolingualEntity, Exception>>()

        val id: EntityId = fixture.fixture()
        val language: EntityId = fixture.fixture()
        val type = EntityModelContract.EntityType.ITEM

        val expected = MonolingualEntity(
            id = id,
            type = type,
            revision = fixture.fixture(),
            language = language,
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = true,
            label = fixture.fixture<String>(),
            description = fixture.fixture<String>(),
            aliases = listOf(fixture.fixture(), fixture.fixture()),
        )

        val expectedEntity = RevisionedEntity(
            id = id,
            type = DataModelContract.EntityType.valueOf(type.name),
            revision = expected.revision,
            lastModification = expected.lastModification,
            labels = mapOf(
                language to LanguageValuePair(language, expected.label!!)
            ),
            descriptions = mapOf(
                language to LanguageValuePair(language, expected.description!!)
            ),
            aliases = mapOf(
                language to listOf(
                    LanguageValuePair(language, expected.aliases.first()),
                    LanguageValuePair(language, expected.aliases.last())
                )
            )
        )

        val remoteEntities = mutableListOf(
            listOf(expectedEntity)
        )

        var capturedRevision: DataModelContract.RevisionedEntity? = null
        client.wikibase.createEntity = { _, givenEntity ->
            capturedRevision = givenEntity as DataModelContract.RevisionedEntity

            SuspendingFunctionWrapperStub { remoteEntities.removeFirst().first() }
        }

        // When
        entityStore.entity.subscribeWithSuspendingFunction { result ->
            actual.send(result)
        }

        // Then
        runBlockingTestWithTimeout {
            actual.receive().error!! fulfils EntityStoreError.InitialState::class
        }

        // When
        entityStore.create(language, type)

        // Then
        runBlockingTestWithTimeout {
            actual.receive().unwrap() mustBe MonolingualEntity(
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

        // When
        entityStore.setLabel(expected.label)

        // Then
        runBlockingTestWithTimeout {
            actual.receive().unwrap() mustBe MonolingualEntity(
                id = "",
                type = type,
                revision = 0,
                language = language,
                lastModification = Instant.DISTANT_PAST,
                isEditable = true,
                label = expected.label,
                description = null,
                aliases = emptyList(),
            )
        }

        // When
        entityStore.setDescription(expected.description)

        // Then
        runBlockingTestWithTimeout {
            actual.receive().unwrap() mustBe MonolingualEntity(
                id = "",
                type = type,
                revision = 0,
                language = language,
                lastModification = Instant.DISTANT_PAST,
                isEditable = true,
                label = expected.label,
                description = expected.description,
                aliases = emptyList(),
            )
        }

        // When
        entityStore.setAliases(expected.aliases)

        // Then
        runBlockingTestWithTimeout {
            actual.receive().unwrap() mustBe MonolingualEntity(
                id = "",
                type = type,
                revision = 0,
                language = language,
                lastModification = Instant.DISTANT_PAST,
                isEditable = true,
                label = expected.label,
                description = expected.description,
                aliases = expected.aliases,
            )
        }

        // When
        entityStore.save()

        // Then
        runBlockingTestWithTimeout {
            actual.receive().unwrap() mustBe expected
            capturedRevision mustBe expectedEntity.copy(
                id = "",
                revision = 0,
                lastModification = Instant.DISTANT_PAST
            )
        }
    }

    @Test
    fun `It fetches, changes and updates Entities`() {
        // Given
        val actual = Channel<ResultContract<EntityModelContract.MonolingualEntity, Exception>>()

        val id: EntityId = fixture.fixture()
        val language: EntityId = fixture.fixture()

        val expected = MonolingualEntity(
            id = id,
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = language,
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = true,
            label = fixture.fixture<String>(),
            description = fixture.fixture<String>(),
            aliases = listOf(fixture.fixture(), fixture.fixture()),
        )

        val fetchedEntity = RevisionedEntity(
            id = id,
            type = DataModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            labels = emptyMap(),
            descriptions = emptyMap(),
            aliases = emptyMap()
        )

        val updatedEntity = RevisionedEntity(
            id = id,
            type = DataModelContract.EntityType.ITEM,
            revision = expected.revision,
            lastModification = expected.lastModification,
            labels = mapOf(
                language to LanguageValuePair(
                    language,
                    expected.label!!
                )
            ),
            descriptions = mapOf(
                language to LanguageValuePair(
                    language,
                    expected.description!!
                )
            ),
            aliases = mapOf(
                language to listOf(
                    LanguageValuePair(
                        language,
                        expected.aliases.first()
                    ),
                    LanguageValuePair(
                        language,
                        expected.aliases.last()
                    )
                )
            )
        )

        val remoteEntities = mutableListOf(
            listOf(fetchedEntity),
            listOf(updatedEntity)
        )

        client.wikibase.fetchEntities = { _, _ ->
            SuspendingFunctionWrapperStub { remoteEntities.removeFirst() }
        }

        var capturedRevision: DataModelContract.RevisionedEntity? = null
        client.wikibase.updateEntity = { givenEntity ->
            capturedRevision = givenEntity

            SuspendingFunctionWrapperStub { remoteEntities.removeFirst().first() }
        }

        client.page.fetchRestrictions = {
            SuspendingFunctionWrapperStub { emptyList() }
        }

        // When
        entityStore.entity.subscribeWithSuspendingFunction { result ->
            actual.send(result)
        }

        // Then
        runBlockingTestWithTimeout {
            actual.receive().error!! fulfils EntityStoreError.InitialState::class
        }

        // When
        entityStore.fetchEntity(id, language)

        // Then
        runBlockingTestWithTimeout {
            actual.receive().isSuccess() mustBe true
        }

        // When
        entityStore.setLabel(expected.label)

        // Then
        runBlockingTestWithTimeout {
            actual.receive().unwrap() mustBe expected.copy(
                revision = fetchedEntity.revision,
                lastModification = fetchedEntity.lastModification,
                description = null,
                aliases = emptyList()
            )
        }

        // When
        entityStore.setDescription(expected.description)

        // Then
        runBlockingTestWithTimeout {
            actual.receive().unwrap() mustBe expected.copy(
                revision = fetchedEntity.revision,
                lastModification = fetchedEntity.lastModification,
                aliases = emptyList()
            )
        }

        // When
        entityStore.setAliases(expected.aliases)

        // Then
        runBlockingTestWithTimeout {
            actual.receive().unwrap() mustBe expected.copy(
                revision = fetchedEntity.revision,
                lastModification = fetchedEntity.lastModification,
            )
        }

        // When
        entityStore.save()

        // Then
        runBlockingTestWithTimeout {
            actual.receive().unwrap() mustBe expected
            capturedRevision mustBe updatedEntity.copy(
                revision = fetchedEntity.revision,
                lastModification = fetchedEntity.lastModification,
            )
        }
    }
}
