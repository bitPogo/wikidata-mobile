/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase

import tech.antibytes.fixture.wikibase.q42
import tech.antibytes.mediawiki.EntityContract
import tech.antibytes.mediawiki.EntityId
import tech.antibytes.mediawiki.LanguageTag
import tech.antibytes.mock.wikibase.WikibaseRepositoryStub
import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fixture.listFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs
import kotlin.test.BeforeTest
import kotlin.test.Test

class WikibaseServiceSpec {
    private val fixture = kotlinFixture()
    private val repository = WikibaseRepositoryStub()

    @BeforeTest
    fun setUp() {
        repository.clear()
    }

    @Test
    fun `It fulfils Service`() {
        WikibaseService(repository) fulfils WikibaseContract.Service::class
    }

    @Test
    fun `Given fetch is called with a Set of Ids, it delegates the call to the Repository and returns its result`() = runBlockingTest {
        // Given
        val ids = fixture.listFixture<EntityId>().toSet()
        val response = listOf(q42)

        var capturedIds: Set<EntityId>? = null
        repository.fetch = { givenIds ->
            capturedIds = givenIds
            response
        }
        // When
        val result = WikibaseService(repository).fetch(ids)

        // Then
        result sameAs response
        capturedIds sameAs ids
    }

    @Test
    fun `Given fetch is called with a SearchTerm, LanguageTag, EntityType and a Limit, it delegates the call to the Repository and returns its result`() = runBlockingTest {
        // Given
        val searchTerm: String = fixture.fixture()
        val languageTag: String = fixture.fixture()
        val type = EntityContract.EntityTypes.PROPERTY
        val limit: Int = fixture.fixture()

        val response = listOf(q42)

        var capturedTerm: String? = null
        var capturedLanguageTag: LanguageTag? = null
        var capturedEntityType: EntityContract.EntityTypes? = null
        var capturedLimit: Int? = null

        repository.search = { givenTerm, givenTag, givenType, givenLimit ->
            capturedTerm = givenTerm
            capturedLanguageTag = givenTag
            capturedEntityType = givenType
            capturedLimit = givenLimit

            response
        }
        // When
        val result = WikibaseService(repository).search(searchTerm, languageTag, type, limit)

        // Then
        result sameAs response
        capturedTerm mustBe searchTerm
        capturedLanguageTag mustBe languageTag
        capturedEntityType mustBe type
        capturedLimit mustBe limit
    }
}
