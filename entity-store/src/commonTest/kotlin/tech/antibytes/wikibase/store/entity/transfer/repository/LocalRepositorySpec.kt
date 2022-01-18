/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.transfer.repository

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
import kotlin.test.Ignore
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
    fun `It fulfils LocalRepository`() {
        LocalRepository(database, mapper) fulfils DomainContract.Repository::class
    }

    // region:fetch
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

        // When
        val result = LocalRepository(database, mapper).fetchEntity(id, language)

        // Then
        result mustBe null
        capturedMonoligualEntityById mustBe id
        capturedLanguage mustBe language
    }

    @Test
    fun `Given fetchEntity is called with an EntityId and a Language it returns null, if no Term is stored`() = runBlockingTest {
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

        mapper.toMonolingualEntity = { _, _, _, _, _, _, _, _, _ -> proofDummy }

        // When
        val result = LocalRepository(database, mapper).fetchEntity(id, language)

        // Then
        result mustBe null
        capturedMonoligualEntityById mustBe id
        capturedLanguage mustBe language
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

    // region:create
    @Test
    fun `Given createEntity is called with an MonolingualEntity, it returns it while adding a Entity and Term Entry`() = runBlockingTest {
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
    fun `Given createEntity is called with an MonolingualEntity, it returns it while adding a cleaned Term Entry, since the Label is empty`() = runBlockingTest {
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
        capturedLabel mustBe null
        capturedDescription mustBe expected.description
        capturedAliases mustBe expected.aliases
    }

    @Test
    fun `Given createEntity is called with an MonolingualEntity, it returns it while cleaning the Term Entry, since the Label has a blank value`() = runBlockingTest {
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
        capturedLabel mustBe null
        capturedDescription mustBe expected.description
        capturedAliases mustBe expected.aliases
    }

    @Test
    fun `Given createEntity is called with an MonolingualEntity, it returns it while adding a cleaned Term Entry, since the Description is empty`() = runBlockingTest {
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
        capturedDescription mustBe null
        capturedAliases mustBe expected.aliases
    }

    @Test
    fun `Given createEntity is called with an MonolingualEntity, it returns it while adding cleaned Term Entry, since the Description has a blank value`() = runBlockingTest {
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
        capturedDescription mustBe null
        capturedAliases mustBe expected.aliases
    }

    @Test
    fun `Given createEntity is called with an MonolingualEntity, it returns it while cleaning the Term Entry, since the Aliases have only empty values`() = runBlockingTest {
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
        capturedAliases mustBe emptyList()
    }

    @Test
    fun `Given createEntity is called with an MonolingualEntity, it returns it while adding a cleaned Term Entry, since the Aliases have only blank values`() = runBlockingTest {
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
        capturedAliases mustBe emptyList()
    }

    // region:update
    @Test
    fun `Given updateEntity is called with an MonolingualEntity, it returns it while adding a Entity and Term Entry, if the Entity was not stored previously`() = runBlockingTest {
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

        val nexts = mutableListOf(true, false)
        val hasEntityCursor = SqlCursorStub { nexts.removeFirst() }
        val hasEntityQuery = QueryStub<Long>(
            { 0 },
            { hasEntityCursor }
        )

        var capturedHasEntityId: String? = null
        database.hasEntity = { givenId ->
            capturedHasEntityId = givenId

            hasEntityQuery
        }

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
        val result = LocalRepository(database, mapper).updateEntity(expected)

        // Then
        result sameAs expected

        capturedHasEntityId mustBe expected.id

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

        val nextHasEntity = mutableListOf(true, false)
        val hasEntityCursor = SqlCursorStub { nextHasEntity.removeFirst() }
        val hasEntityQuery = QueryStub<Long>(
            { 1 },
            { hasEntityCursor }
        )

        var capturedHasEntityId: String? = null
        database.hasEntity = { givenId ->
            capturedHasEntityId = givenId

            hasEntityQuery
        }

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

        capturedHasEntityId mustBe expected.id

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
    fun `Given updateEntity is called with an MonolingualEntity, it returns it while updating the stored Entity and a Term Entry, if the Term is already stored`() = runBlockingTest {
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

        val nextHasEntity = mutableListOf(true, false)
        val hasEntityCursor = SqlCursorStub { nextHasEntity.removeFirst() }
        val hasEntityQuery = QueryStub<Long>(
            { 1 },
            { hasEntityCursor }
        )

        var capturedHasEntityId: String? = null
        database.hasEntity = { givenId ->
            capturedHasEntityId = givenId

            hasEntityQuery
        }

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

        capturedHasEntityId mustBe expected.id

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
    @Ignore // Postponed
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

    // update & update
    @Test
    fun `Given updateEntity is called with an MonolingualEntity, it returns it while updating the stored Entity and cleaning Term since the Label was empty`() = runBlockingTest {
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

        val nextHasEntity = mutableListOf(true, false)
        val hasEntityCursor = SqlCursorStub { nextHasEntity.removeFirst() }
        val hasEntityQuery = QueryStub<Long>(
            { 1 },
            { hasEntityCursor }
        )

        var capturedHasEntityId: String? = null
        database.hasEntity = { givenId ->
            capturedHasEntityId = givenId

            hasEntityQuery
        }

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

        // When
        val result = LocalRepository(database, mapper).updateEntity(expected)

        // Then
        result sameAs expected

        capturedHasEntityId mustBe expected.id

        capturedIdUpdateEntity mustBe expected.id
        capturedRevision mustBe expected.revision
        capturedModification sameAs expected.lastModification
        capturedEditability mustBe expected.isEditable

        capturedHasId mustBe expected.id
        capturedHasLanguage mustBe expected.language

        capturedIdUpdateTerm mustBe expected.id
        capturedUpdateLanguage mustBe expected.language
        capturedLabel mustBe null
        capturedDescription mustBe expected.description
        capturedAliases mustBe expected.aliases
    }

    @Test
    fun `Given updateEntity is called with an MonolingualEntity, it returns it while updating the stored Entity and cleaning the Terms Label since it is blank`() = runBlockingTest {
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

        val nextHasEntity = mutableListOf(true, false)
        val hasEntityCursor = SqlCursorStub { nextHasEntity.removeFirst() }
        val hasEntityQuery = QueryStub<Long>(
            { 1 },
            { hasEntityCursor }
        )

        var capturedHasEntityId: String? = null
        database.hasEntity = { givenId ->
            capturedHasEntityId = givenId

            hasEntityQuery
        }

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

        // When
        val result = LocalRepository(database, mapper).updateEntity(expected)

        // Then
        result sameAs expected

        capturedHasEntityId mustBe expected.id

        capturedIdUpdateEntity mustBe expected.id
        capturedRevision mustBe expected.revision
        capturedModification sameAs expected.lastModification
        capturedEditability mustBe expected.isEditable

        capturedHasId mustBe expected.id
        capturedHasLanguage mustBe expected.language

        capturedIdUpdateTerm mustBe expected.id
        capturedUpdateLanguage mustBe expected.language
        capturedLabel mustBe null
        capturedDescription mustBe expected.description
        capturedAliases mustBe expected.aliases
    }

    @Test
    fun `Given updateEntity is called with an MonolingualEntity, it returns it while updating the stored Entity and cleaning the Terms Description since it is empty`() = runBlockingTest {
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

        val nextHasEntity = mutableListOf(true, false)
        val hasEntityCursor = SqlCursorStub { nextHasEntity.removeFirst() }
        val hasEntityQuery = QueryStub<Long>(
            { 1 },
            { hasEntityCursor }
        )

        var capturedHasEntityId: String? = null
        database.hasEntity = { givenId ->
            capturedHasEntityId = givenId

            hasEntityQuery
        }

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

        // When
        val result = LocalRepository(database, mapper).updateEntity(expected)

        // Then
        result sameAs expected

        capturedHasEntityId mustBe expected.id

        capturedIdUpdateEntity mustBe expected.id
        capturedRevision mustBe expected.revision
        capturedModification sameAs expected.lastModification
        capturedEditability mustBe expected.isEditable

        capturedHasId mustBe expected.id
        capturedHasLanguage mustBe expected.language

        capturedIdUpdateTerm mustBe expected.id
        capturedUpdateLanguage mustBe expected.language
        capturedLabel mustBe expected.label
        capturedDescription mustBe null
        capturedAliases mustBe expected.aliases
    }

    @Test
    fun `Given updateEntity is called with an MonolingualEntity, it returns it while updating the stored Entity and cleaning the Terms Description since it was blank`() = runBlockingTest {
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

        val nextHasEntity = mutableListOf(true, false)
        val hasEntityCursor = SqlCursorStub { nextHasEntity.removeFirst() }
        val hasEntityQuery = QueryStub<Long>(
            { 1 },
            { hasEntityCursor }
        )

        var capturedHasEntityId: String? = null
        database.hasEntity = { givenId ->
            capturedHasEntityId = givenId

            hasEntityQuery
        }

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

        // When
        val result = LocalRepository(database, mapper).updateEntity(expected)

        // Then
        result sameAs expected

        capturedHasEntityId mustBe expected.id

        capturedIdUpdateEntity mustBe expected.id
        capturedRevision mustBe expected.revision
        capturedModification sameAs expected.lastModification
        capturedEditability mustBe expected.isEditable

        capturedHasId mustBe expected.id
        capturedHasLanguage mustBe expected.language

        capturedIdUpdateTerm mustBe expected.id
        capturedUpdateLanguage mustBe expected.language
        capturedLabel mustBe expected.label
        capturedDescription mustBe null
        capturedAliases mustBe expected.aliases
    }

    @Test
    fun `Given updateEntity is called with an MonolingualEntity, it returns it while updating the stored Entity and cleaning the Terms Aliases since they are empty`() = runBlockingTest {
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
            aliases = listOf("", ""),
        )

        val nextHasEntity = mutableListOf(true, false)
        val hasEntityCursor = SqlCursorStub { nextHasEntity.removeFirst() }
        val hasEntityQuery = QueryStub<Long>(
            { 1 },
            { hasEntityCursor }
        )

        var capturedHasEntityId: String? = null
        database.hasEntity = { givenId ->
            capturedHasEntityId = givenId

            hasEntityQuery
        }

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

        // When
        val result = LocalRepository(database, mapper).updateEntity(expected)

        // Then
        result sameAs expected

        capturedHasEntityId mustBe expected.id

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
        capturedAliases mustBe emptyList()
    }

    @Test
    fun `Given updateEntity is called with an MonolingualEntity, it returns it while updating the stored Entit and cleaning the Terms Aliases since they are blank`() = runBlockingTest {
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
            aliases = listOf("  ", " "),
        )

        val nextHasEntity = mutableListOf(true, false)
        val hasEntityCursor = SqlCursorStub { nextHasEntity.removeFirst() }
        val hasEntityQuery = QueryStub<Long>(
            { 1 },
            { hasEntityCursor }
        )

        var capturedHasEntityId: String? = null
        database.hasEntity = { givenId ->
            capturedHasEntityId = givenId

            hasEntityQuery
        }

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

        // When
        val result = LocalRepository(database, mapper).updateEntity(expected)

        // Then
        result sameAs expected

        capturedHasEntityId mustBe expected.id

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
        capturedAliases mustBe emptyList()
    }

    // update & add
    @Test
    fun `Given updateEntity is called with an MonolingualEntity, it returns it while updating the stored Entity and adding a cleaned Term since the Label was empty`() = runBlockingTest {
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

        val nextHasEntity = mutableListOf(true, false)
        val hasEntityCursor = SqlCursorStub { nextHasEntity.removeFirst() }
        val hasEntityQuery = QueryStub<Long>(
            { 1 },
            { hasEntityCursor }
        )

        var capturedHasEntityId: String? = null
        database.hasEntity = { givenId ->
            capturedHasEntityId = givenId

            hasEntityQuery
        }

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

        var capturedAddTermId: String? = null
        var capturedAddTermLanguage: LanguageTag? = null
        var capturedLabel: String? = fixture.fixture<String>()
        var capturedDescription: String? = fixture.fixture<String>()
        var capturedAliases: List<String>? = null
        database.addTerm = { givenId, givenLanguage, givenLabel, givenDescription, givenAliases ->
            capturedAddTermId = givenId
            capturedAddTermLanguage = givenLanguage
            capturedLabel = givenLabel
            capturedDescription = givenDescription
            capturedAliases = givenAliases
        }

        // When
        val result = LocalRepository(database, mapper).updateEntity(expected)

        // Then
        result sameAs expected

        capturedHasEntityId mustBe expected.id

        capturedIdUpdateEntity mustBe expected.id
        capturedRevision mustBe expected.revision
        capturedModification sameAs expected.lastModification
        capturedEditability mustBe expected.isEditable

        capturedHasId mustBe expected.id
        capturedHasLanguage mustBe expected.language

        capturedAddTermId mustBe expected.id
        capturedAddTermLanguage mustBe expected.language
        capturedLabel mustBe null
        capturedDescription mustBe expected.description
        capturedAliases mustBe expected.aliases
    }

    @Test
    fun `Given updateEntity is called with an MonolingualEntity, it returns it while updating the stored Entity and adding a cleaned Term since the Label was blank`() = runBlockingTest {
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

        val nextHasEntity = mutableListOf(true, false)
        val hasEntityCursor = SqlCursorStub { nextHasEntity.removeFirst() }
        val hasEntityQuery = QueryStub<Long>(
            { 1 },
            { hasEntityCursor }
        )

        var capturedHasEntityId: String? = null
        database.hasEntity = { givenId ->
            capturedHasEntityId = givenId

            hasEntityQuery
        }

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

        var capturedAddTermId: String? = null
        var capturedAddTermLanguage: LanguageTag? = null
        var capturedLabel: String? = fixture.fixture<String>()
        var capturedDescription: String? = fixture.fixture<String>()
        var capturedAliases: List<String>? = null
        database.addTerm = { givenId, givenLanguage, givenLabel, givenDescription, givenAliases ->
            capturedAddTermId = givenId
            capturedAddTermLanguage = givenLanguage
            capturedLabel = givenLabel
            capturedDescription = givenDescription
            capturedAliases = givenAliases
        }

        // When
        val result = LocalRepository(database, mapper).updateEntity(expected)

        // Then
        result sameAs expected

        capturedHasEntityId mustBe expected.id

        capturedIdUpdateEntity mustBe expected.id
        capturedRevision mustBe expected.revision
        capturedModification sameAs expected.lastModification
        capturedEditability mustBe expected.isEditable

        capturedHasId mustBe expected.id
        capturedHasLanguage mustBe expected.language

        capturedAddTermId mustBe expected.id
        capturedAddTermLanguage mustBe expected.language
        capturedLabel mustBe null
        capturedDescription mustBe expected.description
        capturedAliases mustBe expected.aliases
    }

    @Test
    fun `Given updateEntity is called with an MonolingualEntity, it returns it while updating the stored Entity and adding a cleaned Term since the Description was empty`() = runBlockingTest {
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

        val nextHasEntity = mutableListOf(true, false)
        val hasEntityCursor = SqlCursorStub { nextHasEntity.removeFirst() }
        val hasEntityQuery = QueryStub<Long>(
            { 1 },
            { hasEntityCursor }
        )

        var capturedHasEntityId: String? = null
        database.hasEntity = { givenId ->
            capturedHasEntityId = givenId

            hasEntityQuery
        }

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

        var capturedAddTermId: String? = null
        var capturedAddTermLanguage: LanguageTag? = null
        var capturedLabel: String? = fixture.fixture<String>()
        var capturedDescription: String? = fixture.fixture<String>()
        var capturedAliases: List<String>? = null
        database.addTerm = { givenId, givenLanguage, givenLabel, givenDescription, givenAliases ->
            capturedAddTermId = givenId
            capturedAddTermLanguage = givenLanguage
            capturedLabel = givenLabel
            capturedDescription = givenDescription
            capturedAliases = givenAliases
        }

        // When
        val result = LocalRepository(database, mapper).updateEntity(expected)

        // Then
        result sameAs expected

        capturedHasEntityId mustBe expected.id

        capturedIdUpdateEntity mustBe expected.id
        capturedRevision mustBe expected.revision
        capturedModification sameAs expected.lastModification
        capturedEditability mustBe expected.isEditable

        capturedHasId mustBe expected.id
        capturedHasLanguage mustBe expected.language

        capturedAddTermId mustBe expected.id
        capturedAddTermLanguage mustBe expected.language
        capturedLabel mustBe expected.label
        capturedDescription mustBe null
        capturedAliases mustBe expected.aliases
    }

    @Test
    fun `Given updateEntity is called with an MonolingualEntity, it returns it while updating the stored Entity and adding a cleaned Term since the Description was blank`() = runBlockingTest {
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

        val nextHasEntity = mutableListOf(true, false)
        val hasEntityCursor = SqlCursorStub { nextHasEntity.removeFirst() }
        val hasEntityQuery = QueryStub<Long>(
            { 1 },
            { hasEntityCursor }
        )

        var capturedHasEntityId: String? = null
        database.hasEntity = { givenId ->
            capturedHasEntityId = givenId

            hasEntityQuery
        }

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

        var capturedAddTermId: String? = null
        var capturedAddTermLanguage: LanguageTag? = null
        var capturedLabel: String? = fixture.fixture<String>()
        var capturedDescription: String? = fixture.fixture<String>()
        var capturedAliases: List<String>? = null
        database.addTerm = { givenId, givenLanguage, givenLabel, givenDescription, givenAliases ->
            capturedAddTermId = givenId
            capturedAddTermLanguage = givenLanguage
            capturedLabel = givenLabel
            capturedDescription = givenDescription
            capturedAliases = givenAliases
        }

        // When
        val result = LocalRepository(database, mapper).updateEntity(expected)

        // Then
        result sameAs expected

        capturedHasEntityId mustBe expected.id

        capturedIdUpdateEntity mustBe expected.id
        capturedRevision mustBe expected.revision
        capturedModification sameAs expected.lastModification
        capturedEditability mustBe expected.isEditable

        capturedHasId mustBe expected.id
        capturedHasLanguage mustBe expected.language

        capturedAddTermId mustBe expected.id
        capturedAddTermLanguage mustBe expected.language
        capturedLabel mustBe expected.label
        capturedDescription mustBe null
        capturedAliases mustBe expected.aliases
    }

    @Test
    fun `Given updateEntity is called with an MonolingualEntity, it returns it while updating the stored Entity and adding a cleaned Term since the Aliases were empty`() = runBlockingTest {
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
            aliases = listOf("", ""),
        )

        val nextHasEntity = mutableListOf(true, false)
        val hasEntityCursor = SqlCursorStub { nextHasEntity.removeFirst() }
        val hasEntityQuery = QueryStub<Long>(
            { 1 },
            { hasEntityCursor }
        )

        var capturedHasEntityId: String? = null
        database.hasEntity = { givenId ->
            capturedHasEntityId = givenId

            hasEntityQuery
        }

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

        var capturedAddTermId: String? = null
        var capturedAddTermLanguage: LanguageTag? = null
        var capturedLabel: String? = fixture.fixture<String>()
        var capturedDescription: String? = fixture.fixture<String>()
        var capturedAliases: List<String>? = null
        database.addTerm = { givenId, givenLanguage, givenLabel, givenDescription, givenAliases ->
            capturedAddTermId = givenId
            capturedAddTermLanguage = givenLanguage
            capturedLabel = givenLabel
            capturedDescription = givenDescription
            capturedAliases = givenAliases
        }

        // When
        val result = LocalRepository(database, mapper).updateEntity(expected)

        // Then
        result sameAs expected

        capturedHasEntityId mustBe expected.id

        capturedIdUpdateEntity mustBe expected.id
        capturedRevision mustBe expected.revision
        capturedModification sameAs expected.lastModification
        capturedEditability mustBe expected.isEditable

        capturedHasId mustBe expected.id
        capturedHasLanguage mustBe expected.language

        capturedAddTermId mustBe expected.id
        capturedAddTermLanguage mustBe expected.language
        capturedLabel mustBe expected.label
        capturedDescription mustBe expected.description
        capturedAliases mustBe emptyList()
    }

    @Test
    fun `Given updateEntity is called with an MonolingualEntity, it returns it while updating the stored Entity and adding a cleaned Term since the Aliases were blank`() = runBlockingTest {
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
            aliases = listOf(" ", "    \n"),
        )

        val nextHasEntity = mutableListOf(true, false)
        val hasEntityCursor = SqlCursorStub { nextHasEntity.removeFirst() }
        val hasEntityQuery = QueryStub<Long>(
            { 1 },
            { hasEntityCursor }
        )

        var capturedHasEntityId: String? = null
        database.hasEntity = { givenId ->
            capturedHasEntityId = givenId

            hasEntityQuery
        }

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

        var capturedAddTermId: String? = null
        var capturedAddTermLanguage: LanguageTag? = null
        var capturedLabel: String? = fixture.fixture<String>()
        var capturedDescription: String? = fixture.fixture<String>()
        var capturedAliases: List<String>? = null
        database.addTerm = { givenId, givenLanguage, givenLabel, givenDescription, givenAliases ->
            capturedAddTermId = givenId
            capturedAddTermLanguage = givenLanguage
            capturedLabel = givenLabel
            capturedDescription = givenDescription
            capturedAliases = givenAliases
        }

        // When
        val result = LocalRepository(database, mapper).updateEntity(expected)

        // Then
        result sameAs expected

        capturedHasEntityId mustBe expected.id

        capturedIdUpdateEntity mustBe expected.id
        capturedRevision mustBe expected.revision
        capturedModification sameAs expected.lastModification
        capturedEditability mustBe expected.isEditable

        capturedHasId mustBe expected.id
        capturedHasLanguage mustBe expected.language

        capturedAddTermId mustBe expected.id
        capturedAddTermLanguage mustBe expected.language
        capturedLabel mustBe expected.label
        capturedDescription mustBe expected.description
        capturedAliases mustBe emptyList()
    }
}
