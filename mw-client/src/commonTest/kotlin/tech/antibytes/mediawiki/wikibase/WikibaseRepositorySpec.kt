/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase

import kotlinx.datetime.Instant
import kotlinx.serialization.json.Json
import tech.antibytes.fixture.wikibase.q42
import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.mediawiki.EntityId
import tech.antibytes.mediawiki.LanguageTag
import tech.antibytes.mediawiki.wikibase.model.EntitiesResponse
import tech.antibytes.mediawiki.wikibase.model.Entity
import tech.antibytes.mediawiki.wikibase.model.EntityResponse
import tech.antibytes.mediawiki.wikibase.model.LanguageValuePair
import tech.antibytes.mediawiki.wikibase.model.Match
import tech.antibytes.mediawiki.wikibase.model.MatchTypes
import tech.antibytes.mediawiki.wikibase.model.SearchEntity
import tech.antibytes.mediawiki.wikibase.model.SearchEntityResponse
import tech.antibytes.mock.ClockStub
import tech.antibytes.mock.serialization.SerializerStub
import tech.antibytes.mock.wikibase.TestEntity
import tech.antibytes.mock.wikibase.WikibaseApiServiceStub
import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fixture.listFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.BeforeTest
import kotlin.test.Test

class WikibaseRepositorySpec {
    private val fixture = kotlinFixture()
    private val apiService = WikibaseApiServiceStub()
    private val boxedTermsSerializer = SerializerStub<DataModelContract.BoxedTerms>()
    private val clock = ClockStub()

    @BeforeTest
    fun setUp() {
        apiService.clear()
        boxedTermsSerializer.clear()
        clock.clear()
    }

    @Test
    fun `It fulfils Repository`() {
        WikibaseRepository(apiService, Json, boxedTermsSerializer, clock) fulfils WikibaseContract.Repository::class
    }

    @Test
    fun `Given fetch is called with a Set of Ids, it delegates the Ids to the ApiService returns a EmptyList if the call was not a Success`() =
        runBlockingTest {
            // Given
            val ids = fixture.listFixture<EntityId>()
            val response = EntitiesResponse(
                entities = mapOf(
                    ids[0] to q42,
                    ids[1] to q42
                ),
                success = 0
            )

            var capturedIds: Set<EntityId>? = null
            var capturedLanguage: LanguageTag? = fixture.fixture<String>()

            apiService.fetch = { givenIds, givenLanguageTag ->
                capturedIds = givenIds
                capturedLanguage = givenLanguageTag

                response
            }

            // When
            val result = WikibaseRepository(apiService, Json, boxedTermsSerializer, clock).fetch(ids.toSet())

            // Then
            result mustBe emptyList()
            capturedIds mustBe ids.toSet()
            capturedLanguage mustBe null
        }

    @Test
    fun `Given fetch is called with a Set of Ids and LanguageTag, it delegates the Ids to the ApiService returns a List of Entities if the call was a Success`() =
        runBlockingTest {
            // Given
            val ids = fixture.listFixture<EntityId>(size = 2)
            val languageTag: String = fixture.fixture()

            val response = EntitiesResponse(
                entities = mapOf(
                    ids[0] to q42,
                    ids[1] to q42
                ),
                success = 1
            )

            var capturedIds: Set<EntityId>? = null
            var capturedLanguage: LanguageTag? = null

            apiService.fetch = { givenIds, givenLanguageTag ->
                capturedIds = givenIds
                capturedLanguage = givenLanguageTag

                response
            }

            // When
            val result = WikibaseRepository(
                apiService,
                Json,
                boxedTermsSerializer,
                clock
            ).fetch(ids.toSet(), languageTag)

            // Then
            result mustBe listOf(q42, q42)
            capturedIds mustBe ids.toSet()
            capturedLanguage mustBe languageTag
        }

    @Test
    fun `Given search is called with a SearchTerm, LanguageTag, EntityType, a Limit and PageIndex, it delegates the Ids to the ApiService returns a EmptyList if the call was not a Success`() =
        runBlockingTest {
            // Given
            val searchTerm: String = fixture.fixture()
            val languageTag: String = fixture.fixture()
            val type = DataModelContract.EntityType.PROPERTY
            val limit: Int = fixture.fixture()
            val page: Int = fixture.fixture()

            val response = SearchEntityResponse(
                search = listOf(
                    SearchEntity(
                        id = fixture.fixture(),
                        label = fixture.fixture(),
                        description = fixture.fixture(),
                        aliases = fixture.listFixture(),
                        match = Match(
                            type = MatchTypes.ALIAS
                        )
                    )
                ),
                success = 0
            )

            var capturedTerm: String? = null
            var capturedLanguageTag: LanguageTag? = null
            var capturedEntityType: DataModelContract.EntityType? = null
            var capturedLimit: Int? = null
            var capturedPageIdx: Int? = null

            apiService.search = { givenTerm, givenTag, givenType, givenLimit, givenPageIdx ->
                capturedTerm = givenTerm
                capturedLanguageTag = givenTag
                capturedEntityType = givenType
                capturedLimit = givenLimit
                capturedPageIdx = givenPageIdx

                response
            }

            // When
            val result = WikibaseRepository(
                apiService,
                Json,
                boxedTermsSerializer,
                clock
            ).search(searchTerm, languageTag, type, limit, page)

            // Then
            result mustBe emptyList()
            capturedTerm mustBe searchTerm
            capturedLanguageTag mustBe languageTag
            capturedEntityType mustBe type
            capturedLimit mustBe limit
            capturedPageIdx mustBe page
        }

    @Test
    fun `Given search is called with a SearchTerm, LanguageTag, EntityType, a Limit and a PageIndex, it delegates the Ids to the ApiService returns a List of Entities if the call was a Success`() =
        runBlockingTest {
            // Given
            val searchTerm: String = fixture.fixture()
            val languageTag: String = fixture.fixture()
            val type = DataModelContract.EntityType.PROPERTY
            val limit: Int = fixture.fixture()
            val page: Int = fixture.fixture()

            val response = SearchEntityResponse(
                search = listOf(
                    SearchEntity(
                        id = fixture.fixture(),
                        label = fixture.fixture(),
                        description = fixture.fixture(),
                        aliases = fixture.listFixture(),
                        match = Match(
                            type = MatchTypes.ALIAS
                        )
                    )
                ),
                success = 1
            )

            var capturedTerm: String? = null
            var capturedLanguageTag: LanguageTag? = null
            var capturedEntityType: DataModelContract.EntityType? = null
            var capturedLimit: Int? = null
            var capturedPageIdx: Int? = null

            apiService.search = { givenTerm, givenTag, givenType, givenLimit, givenPageIdx ->
                capturedTerm = givenTerm
                capturedLanguageTag = givenTag
                capturedEntityType = givenType
                capturedLimit = givenLimit
                capturedPageIdx = givenPageIdx

                response
            }

            // When
            val result = WikibaseRepository(
                apiService,
                Json,
                boxedTermsSerializer,
                clock
            ).search(searchTerm, languageTag, type, limit, page)

            // Then
            val expectedAliases = response.search.first().aliases
                .map { alias ->
                    LanguageValuePair(
                        language = languageTag,
                        value = alias
                    )
                }

            result mustBe listOf(
                Entity(
                    id = response.search.first().id,
                    type = type,
                    labels = mapOf(
                        languageTag to LanguageValuePair(
                            language = languageTag,
                            value = response.search.first().label
                        )
                    ),
                    descriptions = mapOf(
                        languageTag to LanguageValuePair(
                            language = languageTag,
                            value = response.search.first().description
                        )
                    ),
                    aliases = mapOf(
                        languageTag to expectedAliases
                    )
                )
            )
            capturedTerm mustBe searchTerm
            capturedLanguageTag mustBe languageTag
            capturedEntityType mustBe type
            capturedLimit mustBe limit
            capturedPageIdx mustBe page
        }

    @Test
    fun `Given update with a RevisionedEntity and MetaToken, it extracts and delegates all neccessary parameter to ApiService and returns null if the call was not a Success`() =
        runBlockingTest {
            // Given
            val entity = TestEntity(
                id = fixture.fixture(),
                type = DataModelContract.EntityType.ITEM,
                revision = fixture.fixture(),
                lastModification = Instant.DISTANT_FUTURE,
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
                )
            )

            val serializedEntity: String = fixture.fixture()
            val token: String = fixture.fixture()

            val response = EntityResponse(
                entity = q42,
                success = 0
            )

            var capturedId: EntityId? = null
            var capturedRevision: Long? = null
            var capturedSerializedEntity: String? = null
            var capturedToken: String? = null

            apiService.update = { givenId, givenRevision, givenEntity, givenToken ->
                capturedId = givenId
                capturedRevision = givenRevision
                capturedSerializedEntity = givenEntity
                capturedToken = givenToken

                response
            }

            var capturedEntity: DataModelContract.BoxedTerms? = null
            boxedTermsSerializer.serialize = { encoder, givenEntity ->
                capturedEntity = givenEntity

                encoder.encodeString(serializedEntity)
            }

            // When
            val result = WikibaseRepository(apiService, Json, boxedTermsSerializer, clock).update(entity, token)

            // Then
            result mustBe null
            capturedEntity mustBe entity
            capturedSerializedEntity mustBe "\"$serializedEntity\""
            capturedId mustBe entity.id
            capturedRevision mustBe entity.revision
            capturedToken mustBe token
        }

    @Test
    fun `Given update with a RevisionedEntity and MetaToken, it extracts and delegates all neccessary parameter to ApiService and returns a Entity if the call was a Success`() =
        runBlockingTest {
            // Given
            val entity = TestEntity(
                id = fixture.fixture(),
                type = DataModelContract.EntityType.ITEM,
                revision = fixture.fixture(),
                lastModification = Instant.DISTANT_FUTURE,
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
                )
            )

            val modificationDate = Instant.DISTANT_PAST
            val serializedEntity: String = fixture.fixture()
            val token: String = fixture.fixture()

            val response = EntityResponse(
                entity = q42,
                success = 1
            )

            var capturedId: EntityId? = null
            var capturedRevision: Long? = null
            var capturedSerializedEntity: String? = null
            var capturedToken: String? = null

            apiService.update = { givenId, givenRevision, givenEntity, givenToken ->
                capturedId = givenId
                capturedRevision = givenRevision
                capturedSerializedEntity = givenEntity
                capturedToken = givenToken

                response
            }

            var capturedEntity: DataModelContract.BoxedTerms? = null
            boxedTermsSerializer.serialize = { encoder, givenEntity ->
                capturedEntity = givenEntity

                encoder.encodeString(serializedEntity)
            }

            clock.now = { modificationDate }

            // When
            val result = WikibaseRepository(apiService, Json, boxedTermsSerializer, clock).update(entity, token)

            // Then
            result mustBe q42.copy(lastModification = modificationDate)

            capturedEntity mustBe entity
            capturedSerializedEntity mustBe "\"$serializedEntity\""
            capturedId mustBe entity.id
            capturedRevision mustBe entity.revision
            capturedToken mustBe token
        }

    @Test
    fun `Given create with a BoxedTerms and MetaToken, it extracts and delegates all neccessary parameter to ApiService and returns null if the call was not a Success`() =
        runBlockingTest {
            // Given
            val type = DataModelContract.EntityType.ITEM
            val entity = TestEntity(
                id = fixture.fixture(),
                type = DataModelContract.EntityType.ITEM,
                revision = fixture.fixture(),
                lastModification = Instant.DISTANT_FUTURE,
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
                )
            )

            val serializedEntity: String = fixture.fixture()
            val token: String = fixture.fixture()

            val response = EntityResponse(
                entity = q42,
                success = 0
            )

            var capturedType: DataModelContract.EntityType? = null
            var capturedSerializedEntity: String? = null
            var capturedToken: String? = null

            apiService.create = { givenType, givenEntity, givenToken ->
                capturedType = givenType
                capturedSerializedEntity = givenEntity
                capturedToken = givenToken

                response
            }

            var capturedEntity: DataModelContract.BoxedTerms? = null
            boxedTermsSerializer.serialize = { encoder, givenEntity ->
                capturedEntity = givenEntity

                encoder.encodeString(serializedEntity)
            }

            // When
            val result = WikibaseRepository(apiService, Json, boxedTermsSerializer, clock).create(type, entity, token)

            // Then
            result mustBe null
            capturedType mustBe type
            capturedEntity mustBe entity
            capturedSerializedEntity mustBe "\"$serializedEntity\""
            capturedToken mustBe token
        }

    @Test
    fun `Given create with a BoxedTerms and MetaToken, it extracts and delegates all neccessary parameter to ApiService and returns a Entity if the call was a Success`() =
        runBlockingTest {
            // Given
            val type = DataModelContract.EntityType.ITEM
            val entity = TestEntity(
                id = fixture.fixture(),
                type = DataModelContract.EntityType.ITEM,
                revision = fixture.fixture(),
                lastModification = Instant.DISTANT_FUTURE,
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
                )
            )

            val modificationDate = Instant.DISTANT_PAST
            val serializedEntity: String = fixture.fixture()
            val token: String = fixture.fixture()

            val response = EntityResponse(
                entity = q42,
                success = 1
            )

            var capturedType: DataModelContract.EntityType? = null
            var capturedSerializedEntity: String? = null
            var capturedToken: String? = null

            apiService.create = { givenType, givenEntity, givenToken ->
                capturedType = givenType
                capturedSerializedEntity = givenEntity
                capturedToken = givenToken

                response
            }

            var capturedEntity: DataModelContract.BoxedTerms? = null
            boxedTermsSerializer.serialize = { encoder, givenEntity ->
                capturedEntity = givenEntity

                encoder.encodeString(serializedEntity)
            }

            clock.now = { modificationDate }

            // When
            val result = WikibaseRepository(apiService, Json, boxedTermsSerializer, clock).create(type, entity, token)

            // Then
            result mustBe q42.copy(lastModification = modificationDate)
            capturedType mustBe type
            capturedEntity mustBe entity
            capturedSerializedEntity mustBe "\"$serializedEntity\""
            capturedToken mustBe token
        }
}
