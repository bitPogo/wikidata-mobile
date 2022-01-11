/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase

import kotlinx.serialization.json.Json
import tech.antibytes.fixture.wikibase.q42
import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.mediawiki.EntityId
import tech.antibytes.mediawiki.LanguageTag
import tech.antibytes.mediawiki.wikibase.model.LanguageValuePair
import tech.antibytes.mediawiki.wikibase.model.Entity
import tech.antibytes.mediawiki.wikibase.model.EntitiesResponse
import tech.antibytes.mediawiki.wikibase.model.EntityResponse
import tech.antibytes.mediawiki.wikibase.model.Match
import tech.antibytes.mediawiki.wikibase.model.MatchTypes
import tech.antibytes.mediawiki.wikibase.model.SearchEntity
import tech.antibytes.mediawiki.wikibase.model.SearchEntityResponse
import tech.antibytes.mock.ResourceLoader
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

    @BeforeTest
    fun setUp() {
        apiService.clear()
    }

    @Test
    fun `It fulfils Repository`() {
        WikibaseRepository(apiService, Json) fulfils WikibaseContract.Repository::class
    }

    @Test
    fun `Given fetch is called with a Set of Ids, it delegates the Ids to the ApiService returns a EmptyList if the call was not a Success`() = runBlockingTest {
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

        apiService.fetch = { givenIds ->
            capturedIds = givenIds
            response
        }

        // When
        val result = WikibaseRepository(apiService, Json).fetch(ids.toSet())

        // Then
        result mustBe emptyList()
        capturedIds mustBe ids.toSet()
    }

    @Test
    fun `Given fetch is called with a Set of Ids, it delegates the Ids to the ApiService returns a List of Entities if the call was a Success`() = runBlockingTest {
        // Given
        val ids = fixture.listFixture<EntityId>(size = 2)
        val response = EntitiesResponse(
            entities = mapOf(
                ids[0] to q42,
                ids[1] to q42
            ),
            success = 1
        )

        var capturedIds: Set<EntityId>? = null

        apiService.fetch = { givenIds ->
            capturedIds = givenIds
            response
        }

        // When
        val result = WikibaseRepository(apiService, Json).fetch(ids.toSet())

        // Then
        result mustBe listOf(q42, q42)
        capturedIds mustBe ids.toSet()
    }

    @Test
    fun `Given search is called with a SearchTerm, LanguageTag, EntityType and a Limit, it delegates the Ids to the ApiService returns a EmptyList if the call was not a Success`() = runBlockingTest {
        // Given
        val searchTerm: String = fixture.fixture()
        val languageTag: String = fixture.fixture()
        val type = DataModelContract.EntityTypes.PROPERTY
        val limit: Int = fixture.fixture()

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
        var capturedEntityType: DataModelContract.EntityTypes? = null
        var capturedLimit: Int? = null

        apiService.search = { givenTerm, givenTag, givenType, givenLimit ->
            capturedTerm = givenTerm
            capturedLanguageTag = givenTag
            capturedEntityType = givenType
            capturedLimit = givenLimit

            response
        }

        // When
        val result = WikibaseRepository(apiService, Json).search(searchTerm, languageTag, type, limit)

        // Then
        result mustBe emptyList()
        capturedTerm mustBe searchTerm
        capturedLanguageTag mustBe languageTag
        capturedEntityType mustBe type
        capturedLimit mustBe limit
    }

    @Test
    fun `Given search is called with a SearchTerm, LanguageTag, EntityType and a Limit, it delegates the Ids to the ApiService returns a List of Entities if the call was a Success`() = runBlockingTest {
        // Given
        val searchTerm: String = fixture.fixture()
        val languageTag: String = fixture.fixture()
        val type = DataModelContract.EntityTypes.PROPERTY
        val limit: Int = fixture.fixture()

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
        var capturedEntityType: DataModelContract.EntityTypes? = null
        var capturedLimit: Int? = null

        apiService.search = { givenTerm, givenTag, givenType, givenLimit ->
            capturedTerm = givenTerm
            capturedLanguageTag = givenTag
            capturedEntityType = givenType
            capturedLimit = givenLimit

            response
        }

        // When
        val result = WikibaseRepository(apiService, Json).search(searchTerm, languageTag, type, limit)

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
    }

    @Test
    fun `Given update with a RevisionedEntity and MetaToken, it extracts and delegates all neccessary parameter to ApiService and returns null if the call was not a Success`() = runBlockingTest {
        // Given
        val entity = q42
        val token: String = fixture.fixture()
        val expectedSerializedEntity = ResourceLoader.loader.load("/fixture/wikibase/Q42NoEncoding.json")

        val response = EntityResponse(
            entity = q42,
            success = 0
        )

        var capturedId: EntityId? = null
        var capturedRevision: Long? = null
        var capturedEntity: String? = null
        var capturedToken: String? = null

        apiService.update = { givenId, givenRevision, givenEntity, givenToken ->
            capturedId = givenId
            capturedRevision = givenRevision
            capturedEntity = givenEntity
            capturedToken = givenToken

            response
        }

        // When
        val result = WikibaseRepository(apiService, Json).update(entity, token)

        // Then
        result mustBe null
        capturedEntity mustBe expectedSerializedEntity
        capturedId mustBe q42.id
        capturedRevision mustBe q42.revisionId
        capturedToken mustBe token
    }
}
