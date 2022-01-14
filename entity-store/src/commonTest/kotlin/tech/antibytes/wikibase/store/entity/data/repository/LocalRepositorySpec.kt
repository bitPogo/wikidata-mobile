/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.data.repository

import kotlinx.datetime.Instant
import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fixture.listFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs
import tech.antibytes.wikibase.store.entity.domain.DomainContract
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract
import tech.antibytes.wikibase.store.entity.domain.model.LanguageTag
import tech.antibytes.wikibase.store.entity.domain.model.MonolingualEntity
import tech.antibytes.wikibase.store.mock.EntityQueriesStub
import tech.antibytes.wikibase.store.mock.QueryStub
import tech.antibytes.wikibase.store.mock.SqlCursorStub
import tech.antibytes.wikibase.store.mock.data.mapper.LocalEntityMapperStub
import kotlin.test.BeforeTest
import kotlin.test.Test

class LocalRepositorySpec {
    private val fixture = kotlinFixture()
    private val database = EntityQueriesStub()
    private val mapper = LocalEntityMapperStub()

    private val sqlDummy = MonolingualEntity(
        id = fixture.fixture(),
        type = EntityModelContract.EntityType.PROPERTY,
        revision = fixture.fixture(),
        language = fixture.fixture(),
        lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
        isEditable = fixture.fixture(),
        label = null,
        description = null,
        aliases = emptyList(),
    )

    private val proofDummy = MonolingualEntity(
        id = fixture.fixture(),
        type = EntityModelContract.EntityType.LEXEME,
        revision = fixture.fixture(),
        language = fixture.fixture(),
        lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
        isEditable = fixture.fixture(),
        label = null,
        description = null,
        aliases = emptyList(),
    )

    @BeforeTest
    fun setUp() {
        database.clear()
        mapper.clear()
    }

    @Test
    fun `It fulfils Repository`() {
        LocalRepository(database, mapper) fulfils DomainContract.Repository::class
    }

    @Test
    fun `Given fetchEntity is called with an EntityId and a Language it returns null if no Entity is stored`() = runBlockingTest {
        // Given
        val id: String = fixture.fixture()
        val language: String = fixture.fixture()

        val selectMonoligualEntityByIdCursor = SqlCursorStub { false }
        val selectMonoligualEntityByIdQuery = QueryStub<EntityModelContract.MonolingualEntity>(
            { sqlDummy },
            { selectMonoligualEntityByIdCursor }
        )

        var capturedMonoligualEntityById: String? = null
        var capturedLanguage: String? = null
        database.selectMonoligualEntityById = { givenId, givenLangauge, _ ->
            capturedMonoligualEntityById = givenId
            capturedLanguage = givenLangauge

            selectMonoligualEntityByIdQuery
        }

        val selectEntityByIdCursor = SqlCursorStub { false }
        val selectEntityByIdQuery = QueryStub<EntityModelContract.MonolingualEntity>(
            { sqlDummy },
            { selectEntityByIdCursor }
        )

        var capturedEntityById: String? = null
        database.selectEntityById = { givenId, _ ->
            capturedEntityById = givenId

            selectEntityByIdQuery
        }
        // When
        val result = LocalRepository(database, mapper).fetchEntity(id, language)

        // Then
        result mustBe null
        capturedMonoligualEntityById mustBe id
        capturedLanguage mustBe language
        capturedEntityById mustBe id
    }

    @Test
    fun `Given fetchEntity is called with an EntityId and a Language it returns an MonolingualEntity without a Term, if no Term is stored`() = runBlockingTest {
        // Given
        val id: String = fixture.fixture()
        val language: String = fixture.fixture()

        val expected = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = null,
            description = null,
            aliases = emptyList(),
        )

        val selectMonoligualEntityByIdCursor = SqlCursorStub { false }
        val selectMonoligualEntityByIdQuery = QueryStub<EntityModelContract.MonolingualEntity>(
            { sqlDummy },
            { selectMonoligualEntityByIdCursor }
        )

        var capturedMonoligualEntityById: String? = null
        var capturedLanguage: String? = null
        database.selectMonoligualEntityById = { givenId, givenLangauge, _ ->
            capturedMonoligualEntityById = givenId
            capturedLanguage = givenLangauge

            selectMonoligualEntityByIdQuery
        }

        val next = mutableListOf(true, false)
        val selectEntityByIdCursor = SqlCursorStub { next.removeFirst() }
        val selectEntityByIdQuery = QueryStub<EntityModelContract.MonolingualEntity>(
            { expected },
            { selectEntityByIdCursor }
        )

        mapper.toMonolingualEntity = { _, _, _, _, _, _, _, _, _ -> proofDummy }

        var capturedEntityById: String? = null
        var capturedMapper: ((String, EntityModelContract.EntityType, Long, Instant, Boolean) -> EntityModelContract.MonolingualEntity)? = null
        database.selectEntityById = { givenId, givenMapper ->
            capturedEntityById = givenId
            capturedMapper = givenMapper

            selectEntityByIdQuery
        }
        // When
        val result = LocalRepository(database, mapper).fetchEntity(id, language)

        // Then
        result sameAs expected
        capturedMonoligualEntityById mustBe id
        capturedLanguage mustBe language
        capturedEntityById mustBe id
        capturedMapper!!.invoke(
            fixture.fixture(),
            EntityModelContract.EntityType.ITEM,
            fixture.fixture(),
            Instant.DISTANT_PAST,
            fixture.fixture(),
        ) sameAs proofDummy
    }

    @Test
    fun `Given fetchEntity is called with an EntityId and a Language it returns an MonolingualEntity`() = runBlockingTest {
        // Given
        val id: String = fixture.fixture()
        val language: String = fixture.fixture()

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

        val nexts = mutableListOf(true, false)
        val selectMonoligualEntityByIdCursor = SqlCursorStub { nexts.removeFirst() }
        val selectMonoligualEntityByIdQuery = QueryStub<EntityModelContract.MonolingualEntity>(
            { expected },
            { selectMonoligualEntityByIdCursor }
        )

        mapper.toMonolingualEntity = { _, _, _, _, _, _, _, _, _ -> proofDummy }

        var capturedId: String? = null
        var capturedLanguage: String? = null
        var capturedMapper: ((String, EntityModelContract.EntityType, Long, Instant, Boolean, String?, String?, List<String>) -> EntityModelContract.MonolingualEntity)? = null
        database.selectMonoligualEntityById = { givenId, givenLanguage, givenMapper ->
            capturedId = givenId
            capturedLanguage = givenLanguage
            capturedMapper = givenMapper

            selectMonoligualEntityByIdQuery
        }
        // When
        val result = LocalRepository(database, mapper).fetchEntity(id, language)

        // Then
        result sameAs expected
        capturedId mustBe id
        capturedLanguage mustBe language
        capturedMapper!!.invoke(
            fixture.fixture(),
            EntityModelContract.EntityType.ITEM,
            fixture.fixture(),
            Instant.DISTANT_PAST,
            fixture.fixture(),
            fixture.fixture(),
            fixture.fixture(),
            fixture.listFixture(),
        ) sameAs proofDummy
    }

    @Test
    fun `Given createEntity is called with an MonolingualEntity, it returns it while adding a Entity Entry`() = runBlockingTest {
        // Given
        val expected = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = null,
            description = null,
            aliases = emptyList(),
        )

        var capturedId: String? = null
        var capturedType: EntityModelContract.EntityType? = null
        var capturedRevision: Long? = null
        var capturedModification: Instant? = null
        var capturedEditability: Boolean? = null
        database.addEntity = { givenId, givenType, givenRevision, givenModification, givenEditability ->
            capturedId = givenId
            capturedType = givenType
            capturedRevision = givenRevision
            capturedModification = givenModification
            capturedEditability = givenEditability
        }

        // When
        val result = LocalRepository(database, mapper).createEntity(expected)

        // Then
        result sameAs expected
        capturedId mustBe expected.id
        capturedType mustBe expected.type
        capturedRevision mustBe expected.revision
        capturedModification sameAs expected.lastModification
        capturedEditability mustBe expected.isEditable
    }

    @Test
    fun `Given createEntity is called with an MonolingualEntity, it returns it while adding a Entity and Term Entry, if a label is present`() = runBlockingTest {
        // Given
        val expected = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = fixture.fixture(),
            description = null,
            aliases = emptyList(),
        )

        var capturedIdAddEntity: String? = null
        var capturedType: EntityModelContract.EntityType? = null
        var capturedRevision: Long? = null
        var capturedModification: Instant? = null
        var capturedEditability: Boolean? = null
        database.addEntity = { givenId, givenType, givenRevision, givenModification, givenEditability ->
            capturedIdAddEntity = givenId
            capturedType = givenType
            capturedRevision = givenRevision
            capturedModification = givenModification
            capturedEditability = givenEditability
        }

        var capturedIdAddTerm: String? = null
        var capturedLanguage: LanguageTag? = null
        var capturedLabel: String? = null
        var capturedDescription: String? = fixture.fixture<String>()
        var capturedAliases: List<String>? = null
        database.addTerm = { givenId, givenLanguage, givenLabel, givenDescription, givenAliases ->
            capturedIdAddTerm = givenId
            capturedLanguage = givenLanguage
            capturedLabel = givenLabel
            capturedDescription = givenDescription
            capturedAliases = givenAliases
        }

        // When
        val result = LocalRepository(database, mapper).createEntity(expected)

        // Then
        result sameAs expected

        capturedIdAddEntity mustBe expected.id
        capturedType mustBe expected.type
        capturedRevision mustBe expected.revision
        capturedModification sameAs expected.lastModification
        capturedEditability mustBe expected.isEditable

        capturedIdAddTerm mustBe expected.id
        capturedLanguage mustBe expected.language
        capturedLabel mustBe expected.label
        capturedDescription mustBe expected.description
        capturedAliases mustBe expected.aliases
    }

    @Test
    fun `Given createEntity is called with an MonolingualEntity, it returns it while adding a Entity and Term Entry, if a description is present`() = runBlockingTest {
        // Given
        val expected = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = null,
            description = fixture.fixture(),
            aliases = emptyList(),
        )

        var capturedIdAddEntity: String? = null
        var capturedType: EntityModelContract.EntityType? = null
        var capturedRevision: Long? = null
        var capturedModification: Instant? = null
        var capturedEditability: Boolean? = null
        database.addEntity = { givenId, givenType, givenRevision, givenModification, givenEditability ->
            capturedIdAddEntity = givenId
            capturedType = givenType
            capturedRevision = givenRevision
            capturedModification = givenModification
            capturedEditability = givenEditability
        }

        var capturedIdAddTerm: String? = null
        var capturedLanguage: LanguageTag? = null
        var capturedLabel: String? = fixture.fixture<String>()
        var capturedDescription: String? = null
        var capturedAliases: List<String>? = null
        database.addTerm = { givenId, givenLanguage, givenLabel, givenDescription, givenAliases ->
            capturedIdAddTerm = givenId
            capturedLanguage = givenLanguage
            capturedLabel = givenLabel
            capturedDescription = givenDescription
            capturedAliases = givenAliases
        }

        // When
        val result = LocalRepository(database, mapper).createEntity(expected)

        // Then
        result sameAs expected

        capturedIdAddEntity mustBe expected.id
        capturedType mustBe expected.type
        capturedRevision mustBe expected.revision
        capturedModification sameAs expected.lastModification
        capturedEditability mustBe expected.isEditable

        capturedIdAddTerm mustBe expected.id
        capturedLanguage mustBe expected.language
        capturedLabel mustBe expected.label
        capturedDescription mustBe expected.description
        capturedAliases mustBe expected.aliases
    }

    @Test
    fun `Given createEntity is called with an MonolingualEntity, it returns it while adding a Entity and Term Entry, if Aliases are present`() = runBlockingTest {
        // Given
        val expected = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = null,
            description = null,
            aliases = fixture.listFixture(),
        )

        var capturedIdAddEntity: String? = null
        var capturedType: EntityModelContract.EntityType? = null
        var capturedRevision: Long? = null
        var capturedModification: Instant? = null
        var capturedEditability: Boolean? = null
        database.addEntity = { givenId, givenType, givenRevision, givenModification, givenEditability ->
            capturedIdAddEntity = givenId
            capturedType = givenType
            capturedRevision = givenRevision
            capturedModification = givenModification
            capturedEditability = givenEditability
        }

        var capturedIdAddTerm: String? = null
        var capturedLanguage: LanguageTag? = null
        var capturedLabel: String? = fixture.fixture<String>()
        var capturedDescription: String? = fixture.fixture<String>()
        var capturedAliases: List<String>? = null
        database.addTerm = { givenId, givenLanguage, givenLabel, givenDescription, givenAliases ->
            capturedIdAddTerm = givenId
            capturedLanguage = givenLanguage
            capturedLabel = givenLabel
            capturedDescription = givenDescription
            capturedAliases = givenAliases
        }

        // When
        val result = LocalRepository(database, mapper).createEntity(expected)

        // Then
        result sameAs expected

        capturedIdAddEntity mustBe expected.id
        capturedType mustBe expected.type
        capturedRevision mustBe expected.revision
        capturedModification sameAs expected.lastModification
        capturedEditability mustBe expected.isEditable

        capturedIdAddTerm mustBe expected.id
        capturedLanguage mustBe expected.language
        capturedLabel mustBe expected.label
        capturedDescription mustBe expected.description
        capturedAliases mustBe expected.aliases
    }

    @Test
    fun `Given createEntity is called with an MonolingualEntity, it returns it while adding only a Entity Entry, since the Label is empty`() = runBlockingTest {
        // Given
        val expected = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = "",
            description = null,
            aliases = emptyList(),
        )

        var capturedId: String? = null
        var capturedType: EntityModelContract.EntityType? = null
        var capturedRevision: Long? = null
        var capturedModification: Instant? = null
        var capturedEditability: Boolean? = null
        database.addEntity = { givenId, givenType, givenRevision, givenModification, givenEditability ->
            capturedId = givenId
            capturedType = givenType
            capturedRevision = givenRevision
            capturedModification = givenModification
            capturedEditability = givenEditability
        }

        // When
        val result = LocalRepository(database, mapper).createEntity(expected)

        // Then
        result sameAs expected
        capturedId mustBe expected.id
        capturedType mustBe expected.type
        capturedRevision mustBe expected.revision
        capturedModification sameAs expected.lastModification
        capturedEditability mustBe expected.isEditable
    }

    @Test
    fun `Given createEntity is called with an MonolingualEntity, it returns it while adding only a Entity Entry, since the Label has a blank value`() = runBlockingTest {
        // Given
        val expected = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = " ",
            description = null,
            aliases = emptyList(),
        )

        var capturedId: String? = null
        var capturedType: EntityModelContract.EntityType? = null
        var capturedRevision: Long? = null
        var capturedModification: Instant? = null
        var capturedEditability: Boolean? = null
        database.addEntity = { givenId, givenType, givenRevision, givenModification, givenEditability ->
            capturedId = givenId
            capturedType = givenType
            capturedRevision = givenRevision
            capturedModification = givenModification
            capturedEditability = givenEditability
        }

        // When
        val result = LocalRepository(database, mapper).createEntity(expected)

        // Then
        result sameAs expected
        capturedId mustBe expected.id
        capturedType mustBe expected.type
        capturedRevision mustBe expected.revision
        capturedModification sameAs expected.lastModification
        capturedEditability mustBe expected.isEditable
    }

    @Test
    fun `Given createEntity is called with an MonolingualEntity, it returns it while adding only a Entity Entry, since the Description is empty`() = runBlockingTest {
        // Given
        val expected = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = null,
            description = "",
            aliases = emptyList(),
        )

        var capturedId: String? = null
        var capturedType: EntityModelContract.EntityType? = null
        var capturedRevision: Long? = null
        var capturedModification: Instant? = null
        var capturedEditability: Boolean? = null
        database.addEntity = { givenId, givenType, givenRevision, givenModification, givenEditability ->
            capturedId = givenId
            capturedType = givenType
            capturedRevision = givenRevision
            capturedModification = givenModification
            capturedEditability = givenEditability
        }

        // When
        val result = LocalRepository(database, mapper).createEntity(expected)

        // Then
        result sameAs expected
        capturedId mustBe expected.id
        capturedType mustBe expected.type
        capturedRevision mustBe expected.revision
        capturedModification sameAs expected.lastModification
        capturedEditability mustBe expected.isEditable
    }

    @Test
    fun `Given createEntity is called with an MonolingualEntity, it returns it while adding only a Entity Entry, since the Description has a blank value`() = runBlockingTest {
        // Given
        val expected = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = null,
            description = "  ",
            aliases = emptyList(),
        )

        var capturedId: String? = null
        var capturedType: EntityModelContract.EntityType? = null
        var capturedRevision: Long? = null
        var capturedModification: Instant? = null
        var capturedEditability: Boolean? = null
        database.addEntity = { givenId, givenType, givenRevision, givenModification, givenEditability ->
            capturedId = givenId
            capturedType = givenType
            capturedRevision = givenRevision
            capturedModification = givenModification
            capturedEditability = givenEditability
        }

        // When
        val result = LocalRepository(database, mapper).createEntity(expected)

        // Then
        result sameAs expected
        capturedId mustBe expected.id
        capturedType mustBe expected.type
        capturedRevision mustBe expected.revision
        capturedModification sameAs expected.lastModification
        capturedEditability mustBe expected.isEditable
    }

    @Test
    fun `Given createEntity is called with an MonolingualEntity, it returns it while adding only a Entity Entry, since the Aliases have only empty values`() = runBlockingTest {
        // Given
        val expected = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = null,
            description = null,
            aliases = listOf(""),
        )

        var capturedId: String? = null
        var capturedType: EntityModelContract.EntityType? = null
        var capturedRevision: Long? = null
        var capturedModification: Instant? = null
        var capturedEditability: Boolean? = null
        database.addEntity = { givenId, givenType, givenRevision, givenModification, givenEditability ->
            capturedId = givenId
            capturedType = givenType
            capturedRevision = givenRevision
            capturedModification = givenModification
            capturedEditability = givenEditability
        }

        // When
        val result = LocalRepository(database, mapper).createEntity(expected)

        // Then
        result sameAs expected
        capturedId mustBe expected.id
        capturedType mustBe expected.type
        capturedRevision mustBe expected.revision
        capturedModification sameAs expected.lastModification
        capturedEditability mustBe expected.isEditable
    }

    @Test
    fun `Given createEntity is called with an MonolingualEntity, it returns it while adding only a Entity Entry, since the Aliases have only blank values`() = runBlockingTest {
        // Given
        val expected = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = null,
            description = null,
            aliases = listOf("  "),
        )

        var capturedId: String? = null
        var capturedType: EntityModelContract.EntityType? = null
        var capturedRevision: Long? = null
        var capturedModification: Instant? = null
        var capturedEditability: Boolean? = null
        database.addEntity = { givenId, givenType, givenRevision, givenModification, givenEditability ->
            capturedId = givenId
            capturedType = givenType
            capturedRevision = givenRevision
            capturedModification = givenModification
            capturedEditability = givenEditability
        }

        // When
        val result = LocalRepository(database, mapper).createEntity(expected)

        // Then
        result sameAs expected
        capturedId mustBe expected.id
        capturedType mustBe expected.type
        capturedRevision mustBe expected.revision
        capturedModification sameAs expected.lastModification
        capturedEditability mustBe expected.isEditable
    }

    @Test
    fun `Given updateEntity is called with an MonolingualEntity, it returns it while updating the stored Entity and adding a Term Entry`() = runBlockingTest {
        // Given
        val expected = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = fixture.fixture(),
            description = null,
            aliases = emptyList(),
        )

        var capturedIdUpdateEntity: String? = null
        var capturedRevision: Long? = null
        var capturedModification: Instant? = null
        var capturedEditability: Boolean? = null
        database.updateEntity = { givenRevision, givenModification, givenEditability, givenWhereId ->
            capturedIdUpdateEntity = givenWhereId
            capturedRevision = givenRevision
            capturedModification = givenModification
            capturedEditability = givenEditability
        }

        var capturedIdAddTerm: String? = null
        var capturedLanguage: LanguageTag? = null
        var capturedLabel: String? = fixture.fixture<String>()
        var capturedDescription: String? = fixture.fixture<String>()
        var capturedAliases: List<String>? = null
        database.addTerm = { givenId, givenLanguage, givenLabel, givenDescription, givenAliases ->
            capturedIdAddTerm = givenId
            capturedLanguage = givenLanguage
            capturedLabel = givenLabel
            capturedDescription = givenDescription
            capturedAliases = givenAliases
        }

        val nexts = mutableListOf(true, false)
        val selectHasTermCursor = SqlCursorStub { nexts.removeFirst() }
        val selectHasTermQuery = QueryStub<Long>(
            { 0 },
            { selectHasTermCursor }
        )

        var capturedHasId: String? = null
        var capturedHasLanguage: String? = null
        database.hasTerm = { givenId, givenLanguage ->
            capturedHasId = givenId
            capturedHasLanguage = givenLanguage

            selectHasTermQuery
        }

        // When
        val result = LocalRepository(database, mapper).updateEntity(expected)

        // Then
        result sameAs expected

        capturedIdUpdateEntity mustBe expected.id
        capturedRevision mustBe expected.revision
        capturedModification sameAs expected.lastModification
        capturedEditability mustBe expected.isEditable

        capturedHasId mustBe expected.id
        capturedHasLanguage mustBe expected.language

        capturedIdAddTerm mustBe expected.id
        capturedLanguage mustBe expected.language
        capturedLabel mustBe expected.label
        capturedDescription mustBe expected.description
        capturedAliases mustBe expected.aliases
    }

    @Test
    fun `Given updateEntity is called with an MonolingualEntity, it returns it while updating the stored Entity and updates a Term Entry, if the Term is already stored`() = runBlockingTest {
        // Given
        val expected = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = fixture.fixture(),
            description = null,
            aliases = emptyList(),
        )

        var capturedIdUpdateEntity: String? = null
        var capturedRevision: Long? = null
        var capturedModification: Instant? = null
        var capturedEditability: Boolean? = null
        database.updateEntity = { givenRevision, givenModification, givenEditability, givenWhereId ->
            capturedIdUpdateEntity = givenWhereId
            capturedRevision = givenRevision
            capturedModification = givenModification
            capturedEditability = givenEditability
        }

        var capturedIdUpdateTerm: String? = null
        var capturedUpdateLanguage: LanguageTag? = null
        var capturedLabel: String? = fixture.fixture<String>()
        var capturedDescription: String? = fixture.fixture<String>()
        var capturedAliases: List<String>? = null
        database.updateTerm = { givenLabel, givenDescription, givenAliases, givenId, givenLanguage ->
            capturedIdUpdateTerm = givenId
            capturedUpdateLanguage = givenLanguage
            capturedLabel = givenLabel
            capturedDescription = givenDescription
            capturedAliases = givenAliases
        }

        val nexts = mutableListOf(true, false)
        val selectHasTermCursor = SqlCursorStub { nexts.removeFirst() }
        val selectHasTermQuery = QueryStub<Long>(
            { 1 },
            { selectHasTermCursor }
        )

        var capturedHasId: String? = null
        var capturedHasLanguage: String? = null
        database.hasTerm = { givenId, givenLanguage ->
            capturedHasId = givenId
            capturedHasLanguage = givenLanguage

            selectHasTermQuery
        }

        // When
        val result = LocalRepository(database, mapper).updateEntity(expected)

        // Then
        result sameAs expected

        capturedIdUpdateEntity mustBe expected.id
        capturedRevision mustBe expected.revision
        capturedModification sameAs expected.lastModification
        capturedEditability mustBe expected.isEditable

        capturedHasId mustBe expected.id
        capturedHasLanguage mustBe expected.language

        capturedIdUpdateTerm mustBe expected.id
        capturedUpdateLanguage mustBe expected.language
        capturedLabel mustBe expected.label
        capturedDescription mustBe expected.description
        capturedAliases mustBe expected.aliases
    }

    @Test
    fun `Given updateEntity is called with an MonolingualEntity, it returns it while updating the stored Entity and deletes a Term Entry, if the Term is stored and the given Term is empty`() = runBlockingTest {
        // Given
        val expected = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = null,
            description = null,
            aliases = emptyList(),
        )

        var capturedIdUpdateEntity: String? = null
        var capturedRevision: Long? = null
        var capturedModification: Instant? = null
        var capturedEditability: Boolean? = null
        database.updateEntity = { givenRevision, givenModification, givenEditability, givenWhereId ->
            capturedIdUpdateEntity = givenWhereId
            capturedRevision = givenRevision
            capturedModification = givenModification
            capturedEditability = givenEditability
        }

        var capturedIdDeleteTerm: String? = null
        var capturedDeleteLanguage: LanguageTag? = null
        database.deleteTerm = { givenId, givenLanguage ->
            capturedIdDeleteTerm = givenId
            capturedDeleteLanguage = givenLanguage
        }

        val nexts = mutableListOf(true, false)
        val selectHasTermCursor = SqlCursorStub { nexts.removeFirst() }
        val selectHasTermQuery = QueryStub<Long>(
            { 1 },
            { selectHasTermCursor }
        )

        var capturedHasId: String? = null
        var capturedHasLanguage: String? = null
        database.hasTerm = { givenId, givenLanguage ->
            capturedHasId = givenId
            capturedHasLanguage = givenLanguage

            selectHasTermQuery
        }

        // When
        val result = LocalRepository(database, mapper).updateEntity(expected)

        // Then
        result sameAs expected

        capturedIdUpdateEntity mustBe expected.id
        capturedRevision mustBe expected.revision
        capturedModification sameAs expected.lastModification
        capturedEditability mustBe expected.isEditable

        capturedHasId mustBe expected.id
        capturedHasLanguage mustBe expected.language

        capturedIdDeleteTerm mustBe expected.id
        capturedDeleteLanguage mustBe expected.language
    }

    @Test
    fun `Given updateEntity is called with an MonolingualEntity, it returns it while updating the stored Entity, if the Term is not stored and the given Term is empty`() = runBlockingTest {
        // Given
        val expected = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = null,
            description = null,
            aliases = emptyList(),
        )

        var capturedIdUpdateEntity: String? = null
        var capturedRevision: Long? = null
        var capturedModification: Instant? = null
        var capturedEditability: Boolean? = null
        database.updateEntity = { givenRevision, givenModification, givenEditability, givenWhereId ->
            capturedIdUpdateEntity = givenWhereId
            capturedRevision = givenRevision
            capturedModification = givenModification
            capturedEditability = givenEditability
        }

        val nexts = mutableListOf(true, false)
        val selectHasTermCursor = SqlCursorStub { nexts.removeFirst() }
        val selectHasTermQuery = QueryStub<Long>(
            { 0 },
            { selectHasTermCursor }
        )

        var capturedHasId: String? = null
        var capturedHasLanguage: String? = null
        database.hasTerm = { givenId, givenLanguage ->
            capturedHasId = givenId
            capturedHasLanguage = givenLanguage

            selectHasTermQuery
        }

        // When
        val result = LocalRepository(database, mapper).updateEntity(expected)

        // Then
        result sameAs expected

        capturedIdUpdateEntity mustBe expected.id
        capturedRevision mustBe expected.revision
        capturedModification sameAs expected.lastModification
        capturedEditability mustBe expected.isEditable

        capturedHasId mustBe expected.id
        capturedHasLanguage mustBe expected.language
    }

    @Test
    fun `Given updateEntity is called with an MonolingualEntity, it returns it while updating the stored Entity, if the Term is not stored and the given Term Label is empty`() = runBlockingTest {
        // Given
        val expected = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = "",
            description = null,
            aliases = emptyList(),
        )

        var capturedIdUpdateEntity: String? = null
        var capturedRevision: Long? = null
        var capturedModification: Instant? = null
        var capturedEditability: Boolean? = null
        database.updateEntity = { givenRevision, givenModification, givenEditability, givenWhereId ->
            capturedIdUpdateEntity = givenWhereId
            capturedRevision = givenRevision
            capturedModification = givenModification
            capturedEditability = givenEditability
        }

        val nexts = mutableListOf(true, false)
        val selectHasTermCursor = SqlCursorStub { nexts.removeFirst() }
        val selectHasTermQuery = QueryStub<Long>(
            { 0 },
            { selectHasTermCursor }
        )

        var capturedHasId: String? = null
        var capturedHasLanguage: String? = null
        database.hasTerm = { givenId, givenLanguage ->
            capturedHasId = givenId
            capturedHasLanguage = givenLanguage

            selectHasTermQuery
        }

        // When
        val result = LocalRepository(database, mapper).updateEntity(expected)

        // Then
        result sameAs expected

        capturedIdUpdateEntity mustBe expected.id
        capturedRevision mustBe expected.revision
        capturedModification sameAs expected.lastModification
        capturedEditability mustBe expected.isEditable

        capturedHasId mustBe expected.id
        capturedHasLanguage mustBe expected.language
    }

    @Test
    fun `Given updateEntity is called with an MonolingualEntity, it returns it while updating the stored Entity, if the Term is not stored and the given Term Label is blank`() = runBlockingTest {
        // Given
        val expected = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = "  ",
            description = null,
            aliases = emptyList(),
        )

        var capturedIdUpdateEntity: String? = null
        var capturedRevision: Long? = null
        var capturedModification: Instant? = null
        var capturedEditability: Boolean? = null
        database.updateEntity = { givenRevision, givenModification, givenEditability, givenWhereId ->
            capturedIdUpdateEntity = givenWhereId
            capturedRevision = givenRevision
            capturedModification = givenModification
            capturedEditability = givenEditability
        }

        val nexts = mutableListOf(true, false)
        val selectHasTermCursor = SqlCursorStub { nexts.removeFirst() }
        val selectHasTermQuery = QueryStub<Long>(
            { 0 },
            { selectHasTermCursor }
        )

        var capturedHasId: String? = null
        var capturedHasLanguage: String? = null
        database.hasTerm = { givenId, givenLanguage ->
            capturedHasId = givenId
            capturedHasLanguage = givenLanguage

            selectHasTermQuery
        }

        // When
        val result = LocalRepository(database, mapper).updateEntity(expected)

        // Then
        result sameAs expected

        capturedIdUpdateEntity mustBe expected.id
        capturedRevision mustBe expected.revision
        capturedModification sameAs expected.lastModification
        capturedEditability mustBe expected.isEditable

        capturedHasId mustBe expected.id
        capturedHasLanguage mustBe expected.language
    }

    @Test
    fun `Given updateEntity is called with an MonolingualEntity, it returns it while updating the stored Entity, if the Term is not stored and the given Term Description is empty`() = runBlockingTest {
        // Given
        val expected = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = null,
            description = "",
            aliases = emptyList(),
        )

        var capturedIdUpdateEntity: String? = null
        var capturedRevision: Long? = null
        var capturedModification: Instant? = null
        var capturedEditability: Boolean? = null
        database.updateEntity = { givenRevision, givenModification, givenEditability, givenWhereId ->
            capturedIdUpdateEntity = givenWhereId
            capturedRevision = givenRevision
            capturedModification = givenModification
            capturedEditability = givenEditability
        }

        val nexts = mutableListOf(true, false)
        val selectHasTermCursor = SqlCursorStub { nexts.removeFirst() }
        val selectHasTermQuery = QueryStub<Long>(
            { 0 },
            { selectHasTermCursor }
        )

        var capturedHasId: String? = null
        var capturedHasLanguage: String? = null
        database.hasTerm = { givenId, givenLanguage ->
            capturedHasId = givenId
            capturedHasLanguage = givenLanguage

            selectHasTermQuery
        }

        // When
        val result = LocalRepository(database, mapper).updateEntity(expected)

        // Then
        result sameAs expected

        capturedIdUpdateEntity mustBe expected.id
        capturedRevision mustBe expected.revision
        capturedModification sameAs expected.lastModification
        capturedEditability mustBe expected.isEditable

        capturedHasId mustBe expected.id
        capturedHasLanguage mustBe expected.language
    }

    @Test
    fun `Given updateEntity is called with an MonolingualEntity, it returns it while updating the stored Entity, if the Term is not stored and the given Term Description is blank`() = runBlockingTest {
        // Given
        val expected = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = null,
            description = "  ",
            aliases = emptyList(),
        )

        var capturedIdUpdateEntity: String? = null
        var capturedRevision: Long? = null
        var capturedModification: Instant? = null
        var capturedEditability: Boolean? = null
        database.updateEntity = { givenRevision, givenModification, givenEditability, givenWhereId ->
            capturedIdUpdateEntity = givenWhereId
            capturedRevision = givenRevision
            capturedModification = givenModification
            capturedEditability = givenEditability
        }

        val nexts = mutableListOf(true, false)
        val selectHasTermCursor = SqlCursorStub { nexts.removeFirst() }
        val selectHasTermQuery = QueryStub<Long>(
            { 0 },
            { selectHasTermCursor }
        )

        var capturedHasId: String? = null
        var capturedHasLanguage: String? = null
        database.hasTerm = { givenId, givenLanguage ->
            capturedHasId = givenId
            capturedHasLanguage = givenLanguage

            selectHasTermQuery
        }

        // When
        val result = LocalRepository(database, mapper).updateEntity(expected)

        // Then
        result sameAs expected

        capturedIdUpdateEntity mustBe expected.id
        capturedRevision mustBe expected.revision
        capturedModification sameAs expected.lastModification
        capturedEditability mustBe expected.isEditable

        capturedHasId mustBe expected.id
        capturedHasLanguage mustBe expected.language
    }

    @Test
    fun `Given updateEntity is called with an MonolingualEntity, it returns it while updating the stored Entity, if the Term is not stored and the given Term Aliases are empty`() = runBlockingTest {
        // Given
        val expected = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = null,
            description = null,
            aliases = listOf(""),
        )

        var capturedIdUpdateEntity: String? = null
        var capturedRevision: Long? = null
        var capturedModification: Instant? = null
        var capturedEditability: Boolean? = null
        database.updateEntity = { givenRevision, givenModification, givenEditability, givenWhereId ->
            capturedIdUpdateEntity = givenWhereId
            capturedRevision = givenRevision
            capturedModification = givenModification
            capturedEditability = givenEditability
        }

        val nexts = mutableListOf(true, false)
        val selectHasTermCursor = SqlCursorStub { nexts.removeFirst() }
        val selectHasTermQuery = QueryStub<Long>(
            { 0 },
            { selectHasTermCursor }
        )

        var capturedHasId: String? = null
        var capturedHasLanguage: String? = null
        database.hasTerm = { givenId, givenLanguage ->
            capturedHasId = givenId
            capturedHasLanguage = givenLanguage

            selectHasTermQuery
        }

        // When
        val result = LocalRepository(database, mapper).updateEntity(expected)

        // Then
        result sameAs expected

        capturedIdUpdateEntity mustBe expected.id
        capturedRevision mustBe expected.revision
        capturedModification sameAs expected.lastModification
        capturedEditability mustBe expected.isEditable

        capturedHasId mustBe expected.id
        capturedHasLanguage mustBe expected.language
    }

    @Test
    fun `Given updateEntity is called with an MonolingualEntity, it returns it while updating the stored Entity, if the Term is not stored and the given Term Aliases are blank`() = runBlockingTest {
        // Given
        val expected = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            language = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            isEditable = fixture.fixture(),
            label = null,
            description = null,
            aliases = listOf("  "),
        )

        var capturedIdUpdateEntity: String? = null
        var capturedRevision: Long? = null
        var capturedModification: Instant? = null
        var capturedEditability: Boolean? = null
        database.updateEntity = { givenRevision, givenModification, givenEditability, givenWhereId ->
            capturedIdUpdateEntity = givenWhereId
            capturedRevision = givenRevision
            capturedModification = givenModification
            capturedEditability = givenEditability
        }

        val nexts = mutableListOf(true, false)
        val selectHasTermCursor = SqlCursorStub { nexts.removeFirst() }
        val selectHasTermQuery = QueryStub<Long>(
            { 0 },
            { selectHasTermCursor }
        )

        var capturedHasId: String? = null
        var capturedHasLanguage: String? = null
        database.hasTerm = { givenId, givenLanguage ->
            capturedHasId = givenId
            capturedHasLanguage = givenLanguage

            selectHasTermQuery
        }

        // When
        val result = LocalRepository(database, mapper).updateEntity(expected)

        // Then
        result sameAs expected

        capturedIdUpdateEntity mustBe expected.id
        capturedRevision mustBe expected.revision
        capturedModification sameAs expected.lastModification
        capturedEditability mustBe expected.isEditable

        capturedHasId mustBe expected.id
        capturedHasLanguage mustBe expected.language
    }
}
