/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase

import kotlinx.datetime.Instant
import tech.antibytes.fixture.wikibase.q42
import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.mediawiki.EntityId
import tech.antibytes.mediawiki.LanguageTag
import tech.antibytes.mediawiki.core.token.MetaTokenContract
import tech.antibytes.mediawiki.wikibase.model.LanguageValuePair
import tech.antibytes.mock.ServiceResponseWrapperStub
import tech.antibytes.mock.core.token.MetaTokenRepositoryStub
import tech.antibytes.mock.wikibase.TestEntity
import tech.antibytes.mock.wikibase.WikibaseRepositoryStub
import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fixture.listFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs
import kotlin.math.absoluteValue
import kotlin.test.BeforeTest
import kotlin.test.Test

class WikibaseServiceSpec {
    private val fixture = kotlinFixture()
    private val wikibaseRepository = WikibaseRepositoryStub()
    private val tokenRepository = MetaTokenRepositoryStub()
    private val serviceWrapper = ServiceResponseWrapperStub()

    @BeforeTest
    fun setUp() {
        wikibaseRepository.clear()
        serviceWrapper.clear()
    }

    @Test
    fun `It fulfils Service`() {
        WikibaseService(wikibaseRepository, tokenRepository, serviceWrapper) fulfils WikibaseContract.Service::class
    }

    @Test
    fun `Given fetchEntities is called with a Set of Ids, it delegates the call to the Repository and returns its result`() = runBlockingTest {
        // Given
        val ids = fixture.listFixture<EntityId>().toSet()
        val response = listOf(q42)

        var capturedIds: Set<EntityId>? = null
        var capturedLanguage: LanguageTag? = fixture.fixture<String>()

        wikibaseRepository.fetch = { givenIds, givenLanguage ->
            capturedIds = givenIds
            capturedLanguage = givenLanguage

            response
        }
        // When
        val result = WikibaseService(wikibaseRepository, tokenRepository, serviceWrapper).fetchEntities(ids)

        // Then
        result.wrappedFunction.invoke() mustBe response

        serviceWrapper.lastFunction sameAs result.wrappedFunction
        capturedIds sameAs ids
        capturedLanguage mustBe null
    }

    @Test
    fun `Given fetchEntities is called with a Set of Ids and a LanguageTag, it delegates the call to the Repository and returns its result`() = runBlockingTest {
        // Given
        val ids = fixture.listFixture<EntityId>().toSet()
        val language: String = fixture.fixture()
        val response = listOf(q42)

        var capturedIds: Set<EntityId>? = null
        var capturedLanguage: LanguageTag? = null

        wikibaseRepository.fetch = { givenIds, givenLanguage ->
            capturedIds = givenIds
            capturedLanguage = givenLanguage

            response
        }
        // When
        val result = WikibaseService(
            wikibaseRepository,
            tokenRepository,
            serviceWrapper
        ).fetchEntities(ids, language)

        // Then
        result.wrappedFunction.invoke() mustBe response

        serviceWrapper.lastFunction sameAs result.wrappedFunction
        capturedIds sameAs ids
        capturedLanguage mustBe language
    }

    @Test
    fun `Given searchForEntities is called with a SearchTerm, LanguageTag, EntityType and a Limit, it delegates the call to the Repository and returns its result`() = runBlockingTest {
        // Given
        val searchTerm: String = fixture.fixture()
        val languageTag: String = fixture.fixture()
        val type = DataModelContract.EntityType.PROPERTY
        val limit: Int = fixture.fixture()

        val response = listOf(q42)

        var capturedTerm: String? = null
        var capturedLanguageTag: LanguageTag? = null
        var capturedEntityType: DataModelContract.EntityType? = null
        var capturedLimit: Int? = null
        var capturedPageIdx: Int? = null

        wikibaseRepository.search = { givenTerm, givenTag, givenType, givenLimit, givenPageIdx ->
            capturedTerm = givenTerm
            capturedLanguageTag = givenTag
            capturedEntityType = givenType
            capturedLimit = givenLimit
            capturedPageIdx = givenPageIdx

            response
        }
        // When
        val result = WikibaseService(
            wikibaseRepository,
            tokenRepository,
            serviceWrapper
        ).searchForEntities(searchTerm, languageTag, type, limit)

        // Then
        result.wrappedFunction.invoke() mustBe response

        serviceWrapper.lastFunction sameAs result.wrappedFunction
        capturedTerm mustBe searchTerm
        capturedLanguageTag mustBe languageTag
        capturedEntityType mustBe type
        capturedLimit mustBe limit
        capturedPageIdx mustBe 0
    }

    @Test
    fun `Given searchForEntities is called with a SearchTerm, LanguageTag, EntityType, Limit and a PageIndex, it delegates the call to the Repository and returns its result`() = runBlockingTest {
        // Given
        val searchTerm: String = fixture.fixture()
        val languageTag: String = fixture.fixture()
        val type = DataModelContract.EntityType.PROPERTY
        val limit: Int = fixture.fixture()
        val page: Int = fixture.fixture<Int>().absoluteValue

        val response = listOf(q42)

        var capturedTerm: String? = null
        var capturedLanguageTag: LanguageTag? = null
        var capturedEntityType: DataModelContract.EntityType? = null
        var capturedLimit: Int? = null
        var capturedPageIdx: Int? = null

        wikibaseRepository.search = { givenTerm, givenTag, givenType, givenLimit, givenPageIdx ->
            capturedTerm = givenTerm
            capturedLanguageTag = givenTag
            capturedEntityType = givenType
            capturedLimit = givenLimit
            capturedPageIdx = givenPageIdx

            response
        }
        // When
        val result = WikibaseService(
            wikibaseRepository,
            tokenRepository,
            serviceWrapper
        ).searchForEntities(searchTerm, languageTag, type, limit, page)

        // Then
        result.wrappedFunction.invoke() mustBe response

        serviceWrapper.lastFunction sameAs result.wrappedFunction
        capturedTerm mustBe searchTerm
        capturedLanguageTag mustBe languageTag
        capturedEntityType mustBe type
        capturedLimit mustBe limit
        capturedPageIdx mustBe page
    }

    @Test
    fun `Given searchForEntities is called with a SearchTerm, LanguageTag, EntityType, Limit and a PageIndex, it filters the Index and delegates the call to the Repository and returns its result`() = runBlockingTest {
        // Given
        val searchTerm: String = fixture.fixture()
        val languageTag: String = fixture.fixture()
        val type = DataModelContract.EntityType.PROPERTY
        val limit: Int = fixture.fixture()
        val page: Int = -1 * fixture.fixture<Int>().absoluteValue

        val response = listOf(q42)

        var capturedTerm: String? = null
        var capturedLanguageTag: LanguageTag? = null
        var capturedEntityType: DataModelContract.EntityType? = null
        var capturedLimit: Int? = null
        var capturedPageIdx: Int? = null

        wikibaseRepository.search = { givenTerm, givenTag, givenType, givenLimit, givenPageIdx ->
            capturedTerm = givenTerm
            capturedLanguageTag = givenTag
            capturedEntityType = givenType
            capturedLimit = givenLimit
            capturedPageIdx = givenPageIdx

            response
        }
        // When
        val result = WikibaseService(
            wikibaseRepository,
            tokenRepository,
            serviceWrapper
        ).searchForEntities(searchTerm, languageTag, type, limit, page)

        // Then
        result.wrappedFunction.invoke() mustBe response

        serviceWrapper.lastFunction sameAs result.wrappedFunction
        capturedTerm mustBe searchTerm
        capturedLanguageTag mustBe languageTag
        capturedEntityType mustBe type
        capturedLimit mustBe limit
        capturedPageIdx mustBe 0
    }

    @Test
    fun `Given updateEntity is called with a RevisionedEntity, it retrieves a EditToken and delegates the call to the Repository and returns its result`() = runBlockingTest {
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

        val token: String = fixture.fixture()
        val response = q42

        var capturedTokenType: MetaTokenContract.MetaTokenType? = null
        tokenRepository.fetchToken = { givenTokenType ->
            capturedTokenType = givenTokenType

            token
        }

        var capturedEntity: DataModelContract.RevisionedEntity? = null
        var capturedToken: String? = null
        wikibaseRepository.update = { givenEntity, givenToken ->
            capturedEntity = givenEntity
            capturedToken = givenToken

            response
        }
        // When
        val result = WikibaseService(wikibaseRepository, tokenRepository, serviceWrapper).updateEntity(entity)

        // Then
        result.wrappedFunction.invoke() mustBe response

        serviceWrapper.lastFunction sameAs result.wrappedFunction
        capturedTokenType mustBe MetaTokenContract.MetaTokenType.CSRF
        capturedEntity sameAs entity
        capturedToken sameAs token
    }

    @Test
    fun `Given createEntity is called with a EntityType, BoxedTerms, it retrieves a EditToken and delegates the call to the Repository and returns its result`() = runBlockingTest {
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

        val token: String = fixture.fixture()
        val response = q42

        var capturedTokenType: MetaTokenContract.MetaTokenType? = null
        tokenRepository.fetchToken = { givenTokenType ->
            capturedTokenType = givenTokenType

            token
        }

        var capturedEntity: DataModelContract.BoxedTerms? = null
        var capturedToken: String? = null
        var capturedEntityType: DataModelContract.EntityType? = null
        wikibaseRepository.create = { givenType, givenEntity, givenToken ->
            capturedEntityType = givenType
            capturedEntity = givenEntity
            capturedToken = givenToken

            response
        }
        // When
        val result = WikibaseService(wikibaseRepository, tokenRepository, serviceWrapper).createEntity(type, entity)

        // Then
        result.wrappedFunction.invoke() mustBe response

        serviceWrapper.lastFunction sameAs result.wrappedFunction
        capturedTokenType mustBe MetaTokenContract.MetaTokenType.CSRF
        capturedEntityType sameAs type
        capturedEntity sameAs entity
        capturedToken sameAs token
    }
}
