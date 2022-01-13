/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.data.repository

import kotlinx.datetime.Instant
import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fixture.listFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs
import tech.antibytes.wikibase.store.entity.data.dto.LanguageValuePair
import tech.antibytes.wikibase.store.entity.data.dto.RevisionedEntity
import tech.antibytes.wikibase.store.entity.domain.DomainContract
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract
import tech.antibytes.wikibase.store.entity.domain.model.LanguageTag
import tech.antibytes.wikibase.store.entity.domain.model.MonolingualEntity
import tech.antibytes.wikibase.store.entity.lang.EntityStoreError
import tech.antibytes.wikibase.store.mock.MwClientStub
import tech.antibytes.wikibase.store.mock.SuspendingFunctionWrapperStub
import tech.antibytes.wikibase.store.mock.data.mapper.RemoteEntityMapperStub
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class RemoteRepositorySpec {
    private val fixture = kotlinFixture()
    private val client = MwClientStub()
    private val mapper = RemoteEntityMapperStub()

    @BeforeTest
    fun setUp() {
        client.clear()
        mapper.clear()
    }

    @Test
    fun `It fulfils Repository`() {
        RemoteRepository(client, mapper) fulfils DomainContract.Repository::class
    }

    @Test
    fun `Given fetchEntity is called with a EntityId and a LanguageTage, it fails due to a empty Response`() = runBlockingTest {
        // Given
        val id: String = fixture.fixture()
        val language: String = fixture.fixture()

        var capturedIds: Set<String>? = null
        var capturedLanguage: LanguageTag? = null
        client.wikibase.fetchEntities = { givenIds, givenLanguage ->
            capturedIds = givenIds
            capturedLanguage = givenLanguage

            SuspendingFunctionWrapperStub(
                suspend { emptyList() }
            )
        }
        // Then
        val error = assertFailsWith<EntityStoreError.MissingEntity> {
            // When
            RemoteRepository(client, mapper).fetchEntity(id, language)
        }

        error.message mustBe "Missing Entity ($id) in Language ($language)"
        capturedIds mustBe setOf(id)
        capturedLanguage mustBe language
    }

    @Test
    fun `Given fetchEntity is called with a EntityId and a LanguageTage, it returns a MonolingualEntity`() = runBlockingTest {
        // Given
        val id: String = fixture.fixture()
        val language: String = fixture.fixture()

        val revisionedEntity = RevisionedEntity(
            id = fixture.fixture(),
            type = DataModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            labels = mapOf(
                fixture.fixture<String>() to LanguageValuePair(
                    language = fixture.fixture(),
                    value = fixture.fixture()
                )
            ),
            descriptions = mapOf(
                fixture.fixture<String>() to LanguageValuePair(
                    language = fixture.fixture(),
                    value = fixture.fixture()
                )
            ),
            aliases = mapOf(
                fixture.fixture<String>() to listOf(
                    LanguageValuePair(
                        language = fixture.fixture(),
                        value = fixture.fixture()
                    )
                )
            ),
        )

        val expectedEntity = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            isEditable = fixture.fixture(),
            label = null,
            description = null,
            aliases = emptyList(),
        )

        val restrictions: List<String> = fixture.listFixture()

        var capturedIds: Set<String>? = null
        var capturedLanguage: LanguageTag? = null
        client.wikibase.fetchEntities = { givenIds, givenLanguage ->
            capturedIds = givenIds
            capturedLanguage = givenLanguage

            SuspendingFunctionWrapperStub(
                suspend { listOf(revisionedEntity) }
            )
        }

        var capturedRestrictionId: String? = null
        client.page.fetchRestrictions = { givenId ->
            capturedRestrictionId = givenId

            SuspendingFunctionWrapperStub(
                suspend { restrictions }
            )
        }

        var capturedMapperLanguage: String? = null
        var capturedRevisionEntity: DataModelContract.RevisionedEntity? = null
        var capturedRestrictions: List<String>? = null
        mapper.toMonolingualEntity = { givenLanguage, givenEntity, givenRestrictions ->
            capturedMapperLanguage = givenLanguage
            capturedRevisionEntity = givenEntity
            capturedRestrictions = givenRestrictions

            expectedEntity
        }

        // When
        val entity = RemoteRepository(client, mapper).fetchEntity(id, language)

        // Then
        entity sameAs expectedEntity
        capturedIds mustBe setOf(id)
        capturedLanguage mustBe language

        capturedRestrictionId mustBe id

        capturedMapperLanguage mustBe language
        capturedRevisionEntity mustBe revisionedEntity
        capturedRestrictions mustBe restrictions
    }

    @Test
    fun `Given createEntity is called with a MonolingualEntity, it fails due to a empty Response`() = runBlockingTest {
        // Given
        val monolingualEntity = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            isEditable = fixture.fixture(),
            label = null,
            description = null,
            aliases = emptyList(),
        )

        val outGoingRevisionedEntity = RevisionedEntity(
            id = fixture.fixture(),
            type = DataModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            labels = mapOf(
                fixture.fixture<String>() to LanguageValuePair(
                    language = fixture.fixture(),
                    value = fixture.fixture()
                )
            ),
            descriptions = mapOf(
                fixture.fixture<String>() to LanguageValuePair(
                    language = fixture.fixture(),
                    value = fixture.fixture()
                )
            ),
            aliases = mapOf(
                fixture.fixture<String>() to listOf(
                    LanguageValuePair(
                        language = fixture.fixture(),
                        value = fixture.fixture()
                    )
                )
            ),
        )

        var capturedMonolingualEntity: EntityModelContract.MonolingualEntity? = null
        mapper.toRevisionedEntity = { givenEntity ->
            capturedMonolingualEntity = givenEntity

            outGoingRevisionedEntity
        }

        var capturedOutGoingEntity: DataModelContract.BoxedTerms? = null
        var capturedEntityType: DataModelContract.EntityType? = null
        client.wikibase.createEntity = { givenType, givenEntity ->
            capturedOutGoingEntity = givenEntity
            capturedEntityType = givenType

            SuspendingFunctionWrapperStub(
                suspend { null }
            )
        }

        // Then
        val error = assertFailsWith<EntityStoreError.CreationFailure> {
            // When
           RemoteRepository(client, mapper).createEntity(monolingualEntity)
        }

        error.message mustBe "Cannot create Entity in Language (${monolingualEntity.language})"

        capturedMonolingualEntity sameAs monolingualEntity
        capturedOutGoingEntity sameAs outGoingRevisionedEntity
        capturedEntityType sameAs outGoingRevisionedEntity.type
    }

    @Test
    fun `Given createEntity is called with a MonolingualEntity, it returns a MonolingualEntity`() = runBlockingTest {
        // Given
        val outGoingMonolingualEntity = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            isEditable = fixture.fixture(),
            label = null,
            description = null,
            aliases = emptyList(),
        )

        val expected = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            isEditable = fixture.fixture(),
            label = null,
            description = null,
            aliases = emptyList(),
        )

        val outGoingRevisionedEntity = RevisionedEntity(
            id = fixture.fixture(),
            type = DataModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            labels = mapOf(
                fixture.fixture<String>() to LanguageValuePair(
                    language = fixture.fixture(),
                    value = fixture.fixture()
                )
            ),
            descriptions = mapOf(
                fixture.fixture<String>() to LanguageValuePair(
                    language = fixture.fixture(),
                    value = fixture.fixture()
                )
            ),
            aliases = mapOf(
                fixture.fixture<String>() to listOf(
                    LanguageValuePair(
                        language = fixture.fixture(),
                        value = fixture.fixture()
                    )
                )
            ),
        )

        val inComingRevisionedEntity = RevisionedEntity(
            id = fixture.fixture(),
            type = DataModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            labels = mapOf(
                fixture.fixture<String>() to LanguageValuePair(
                    language = fixture.fixture(),
                    value = fixture.fixture()
                )
            ),
            descriptions = mapOf(
                fixture.fixture<String>() to LanguageValuePair(
                    language = fixture.fixture(),
                    value = fixture.fixture()
                )
            ),
            aliases = mapOf(
                fixture.fixture<String>() to listOf(
                    LanguageValuePair(
                        language = fixture.fixture(),
                        value = fixture.fixture()
                    )
                )
            ),
        )

        var capturedMonolingualEntity: EntityModelContract.MonolingualEntity? = null
        mapper.toRevisionedEntity = { givenEntity ->
            capturedMonolingualEntity = givenEntity

            outGoingRevisionedEntity
        }

        var capturedOutGoingEntity: DataModelContract.BoxedTerms? = null
        var capturedEntityType: DataModelContract.EntityType? = null
        client.wikibase.createEntity = { givenType, givenEntity ->
            capturedOutGoingEntity = givenEntity
            capturedEntityType = givenType

            SuspendingFunctionWrapperStub(
                suspend { inComingRevisionedEntity }
            )
        }

        var capturedMapperLanguage: String? = null
        var capturedInComingRevisionEntity: DataModelContract.RevisionedEntity? = null
        var capturedRestrictions: List<String>? = null
        mapper.toMonolingualEntity = { givenLanguage, givenEntity, givenRestrictions ->
            capturedMapperLanguage = givenLanguage
            capturedInComingRevisionEntity = givenEntity
            capturedRestrictions = givenRestrictions

            expected
        }

        // When
        val actual = RemoteRepository(client, mapper).createEntity(outGoingMonolingualEntity)

        actual sameAs expected

        capturedMonolingualEntity sameAs outGoingMonolingualEntity
        capturedOutGoingEntity sameAs outGoingRevisionedEntity
        capturedEntityType sameAs outGoingRevisionedEntity.type
        capturedMapperLanguage mustBe outGoingMonolingualEntity.language
        capturedInComingRevisionEntity sameAs inComingRevisionedEntity
        capturedRestrictions mustBe emptyList()
    }

    @Test
    fun `Given updateEntity is called with a MonolingualEntity, it fails due to a empty Response`() = runBlockingTest {
        // Given
        val monolingualEntity = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            isEditable = fixture.fixture(),
            label = null,
            description = null,
            aliases = emptyList(),
        )

        val outGoingRevisionedEntity = RevisionedEntity(
            id = fixture.fixture(),
            type = DataModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            labels = mapOf(
                fixture.fixture<String>() to LanguageValuePair(
                    language = fixture.fixture(),
                    value = fixture.fixture()
                )
            ),
            descriptions = mapOf(
                fixture.fixture<String>() to LanguageValuePair(
                    language = fixture.fixture(),
                    value = fixture.fixture()
                )
            ),
            aliases = mapOf(
                fixture.fixture<String>() to listOf(
                    LanguageValuePair(
                        language = fixture.fixture(),
                        value = fixture.fixture()
                    )
                )
            ),
        )

        var capturedMonolingualEntity: EntityModelContract.MonolingualEntity? = null
        mapper.toRevisionedEntity = { givenEntity ->
            capturedMonolingualEntity = givenEntity

            outGoingRevisionedEntity
        }

        var capturedOutGoingEntity: DataModelContract.RevisionedEntity? = null
        client.wikibase.updateEntity = { givenEntity ->
            capturedOutGoingEntity = givenEntity

            SuspendingFunctionWrapperStub(
                suspend { null }
            )
        }

        // Then
        val error = assertFailsWith<EntityStoreError.EditFailure> {
            // When
            RemoteRepository(client, mapper).updateEntity(monolingualEntity)
        }

        error.message mustBe "Cannot edit Entity (${outGoingRevisionedEntity.id}) in Language (${monolingualEntity.language})"

        capturedMonolingualEntity sameAs monolingualEntity
        capturedOutGoingEntity sameAs outGoingRevisionedEntity
    }

    @Test
    fun `Given updateEntity is called with a MonolingualEntity, it returns a MonolingualEntity`() = runBlockingTest {
        // Given
        val outGoingMonolingualEntity = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            isEditable = fixture.fixture(),
            label = null,
            description = null,
            aliases = emptyList(),
        )

        val expected = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            isEditable = fixture.fixture(),
            label = null,
            description = null,
            aliases = emptyList(),
        )

        val outGoingRevisionedEntity = RevisionedEntity(
            id = fixture.fixture(),
            type = DataModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            labels = mapOf(
                fixture.fixture<String>() to LanguageValuePair(
                    language = fixture.fixture(),
                    value = fixture.fixture()
                )
            ),
            descriptions = mapOf(
                fixture.fixture<String>() to LanguageValuePair(
                    language = fixture.fixture(),
                    value = fixture.fixture()
                )
            ),
            aliases = mapOf(
                fixture.fixture<String>() to listOf(
                    LanguageValuePair(
                        language = fixture.fixture(),
                        value = fixture.fixture()
                    )
                )
            ),
        )

        val inComingRevisionedEntity = RevisionedEntity(
            id = fixture.fixture(),
            type = DataModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            labels = mapOf(
                fixture.fixture<String>() to LanguageValuePair(
                    language = fixture.fixture(),
                    value = fixture.fixture()
                )
            ),
            descriptions = mapOf(
                fixture.fixture<String>() to LanguageValuePair(
                    language = fixture.fixture(),
                    value = fixture.fixture()
                )
            ),
            aliases = mapOf(
                fixture.fixture<String>() to listOf(
                    LanguageValuePair(
                        language = fixture.fixture(),
                        value = fixture.fixture()
                    )
                )
            ),
        )

        var capturedMonolingualEntity: EntityModelContract.MonolingualEntity? = null
        mapper.toRevisionedEntity = { givenEntity ->
            capturedMonolingualEntity = givenEntity

            outGoingRevisionedEntity
        }

        var capturedOutGoingEntity: DataModelContract.RevisionedEntity? = null
        client.wikibase.updateEntity = { givenEntity ->
            capturedOutGoingEntity = givenEntity

            SuspendingFunctionWrapperStub(
                suspend { inComingRevisionedEntity }
            )
        }

        var capturedMapperLanguage: String? = null
        var capturedInComingRevisionEntity: DataModelContract.RevisionedEntity? = null
        var capturedRestrictions: List<String>? = null
        mapper.toMonolingualEntity = { givenLanguage, givenEntity, givenRestrictions ->
            capturedMapperLanguage = givenLanguage
            capturedInComingRevisionEntity = givenEntity
            capturedRestrictions = givenRestrictions

            expected
        }

        // When
        val actual = RemoteRepository(client, mapper).updateEntity(outGoingMonolingualEntity)

        actual sameAs expected

        capturedMonolingualEntity sameAs outGoingMonolingualEntity
        capturedOutGoingEntity sameAs outGoingRevisionedEntity
        capturedMapperLanguage mustBe outGoingMonolingualEntity.language
        capturedInComingRevisionEntity sameAs inComingRevisionedEntity
        capturedRestrictions mustBe emptyList()
    }
}
