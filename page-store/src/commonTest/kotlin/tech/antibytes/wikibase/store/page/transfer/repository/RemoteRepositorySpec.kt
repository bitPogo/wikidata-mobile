/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.page.transfer.repository

import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fixture.listFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.wikibase.store.mock.SuspendingFunctionWrapperStub
import tech.antibytes.wikibase.store.mock.client.EntityStub
import tech.antibytes.wikibase.store.mock.client.MwClientStub
import tech.antibytes.wikibase.store.mock.client.PagePointerStub
import tech.antibytes.wikibase.store.mock.transfer.mapper.SearchEntityMapperStub
import tech.antibytes.wikibase.store.page.domain.DomainContract
import tech.antibytes.wikibase.store.page.domain.model.SearchEntry
import kotlin.test.BeforeTest
import kotlin.test.Test

class RemoteRepositorySpec {
    private val fixture = kotlinFixture()
    private val client = MwClientStub()
    private val mapper = SearchEntityMapperStub()

    @BeforeTest
    fun setUp() {
        client.clear()
        mapper.clear()
    }

    @Test
    fun `It fulfils RemoteRepository`() {
        RemoteRepository(client, mapper) fulfils DomainContract.RemoteRepository::class
    }

    @Test
    fun `Given fetchRandomPageIds is called, it returns a List of EntityIds`() = runBlockingTest {
        // Given
        val expected = fixture.listFixture<String>().map { id -> "Q$id" }

        val randomPages = expected.map { id -> PagePointerStub(fixture.fixture(), id) }

        var limit: Int? = null
        var namespace: Int? = fixture.fixture<Int>()
        client.page.randomPage = { givenLimit, givenNamespace ->
            limit = givenLimit
            namespace = givenNamespace

            SuspendingFunctionWrapperStub { randomPages }
        }
        // When
        val actual = RemoteRepository(client, mapper).fetchRandomItemIds()

        //
        actual mustBe expected

        limit mustBe 42
        namespace mustBe null
    }

    @Test
    fun `Given fetchRandomPageIds is called, it filtes non Items of List of EntityIds`() = runBlockingTest {
        // Given
        val expected = fixture.listFixture<String>()

        val randomPages = expected.map { id -> PagePointerStub(fixture.fixture(), id) }

        var limit: Int? = null
        var namespace: Int? = fixture.fixture<Int>()
        client.page.randomPage = { givenLimit, givenNamespace ->
            limit = givenLimit
            namespace = givenNamespace

            SuspendingFunctionWrapperStub { randomPages }
        }
        // When
        val actual = RemoteRepository(client, mapper).fetchRandomItemIds()

        //
        actual mustBe emptyList()

        limit mustBe 42
        namespace mustBe null
    }

    @Test
    fun `Given searchForItem is called with a SearchTerm and Language, it returns a List of Search Entries`() = runBlockingTest {
        // Given
        val searchTerm: String = fixture.fixture()
        val language: String = fixture.fixture()

        val expected = listOf(
            SearchEntry(
                id = fixture.fixture(),
                language = fixture.fixture(),
                label = fixture.fixture(),
                description = fixture.fixture()
            ),
            SearchEntry(
                id = fixture.fixture(),
                language = fixture.fixture(),
                label = fixture.fixture(),
                description = fixture.fixture()
            )
        )

        val entities = listOf(
            EntityStub(
                id = fixture.fixture(),
                type = DataModelContract.EntityType.ITEM,
                labels = emptyMap(),
                descriptions = emptyMap(),
                aliases = emptyMap()
            ),
            EntityStub(
                id = fixture.fixture(),
                type = DataModelContract.EntityType.ITEM,
                labels = emptyMap(),
                descriptions = emptyMap(),
                aliases = emptyMap()
            )
        )

        var capturedTerm: String? = null
        var capturedLanguage: String? = null
        var capturedType: DataModelContract.EntityType? = null
        var capturedLimit: Int? = null
        var capturedPage: Int? = null
        client.wikibase.searchForEntities = { givenSearchTerm, givenLanguage, givenType, givenLimit, givenPageIdx ->
            capturedTerm = givenSearchTerm
            capturedLanguage = givenLanguage
            capturedType = givenType
            capturedLimit = givenLimit
            capturedPage = givenPageIdx

            SuspendingFunctionWrapperStub { entities }
        }

        val mappedEntities = expected.toMutableList()

        val capturedEntities: MutableList<DataModelContract.Entity> = mutableListOf()
        mapper.map = { givenEntity ->
            capturedEntities.add(givenEntity)

            mappedEntities.removeFirst()
        }

        // When
        val actual = RemoteRepository(client, mapper).searchForItem(searchTerm, language)

        // Then
        actual mustBe expected

        capturedEntities mustBe capturedEntities

        capturedTerm mustBe searchTerm
        capturedLanguage mustBe language
        capturedType mustBe DataModelContract.EntityType.ITEM
        capturedLimit mustBe 50
        capturedPage mustBe 0
    }

    @Test
    fun `Given searchForItem is called with a SearchTerm and Language, it returns a List of Search Entries, while filerting empty values`() = runBlockingTest {
        // Given
        val searchTerm: String = fixture.fixture()
        val language: String = fixture.fixture()

        val expected = listOf(
            SearchEntry(
                id = fixture.fixture(),
                language = fixture.fixture(),
                label = fixture.fixture(),
                description = fixture.fixture()
            ),
            SearchEntry(
                id = fixture.fixture(),
                language = fixture.fixture(),
                label = fixture.fixture(),
                description = fixture.fixture()
            )
        )

        val entities = listOf(
            EntityStub(
                id = fixture.fixture(),
                type = DataModelContract.EntityType.ITEM,
                labels = emptyMap(),
                descriptions = emptyMap(),
                aliases = emptyMap()
            ),
            EntityStub(
                id = fixture.fixture(),
                type = DataModelContract.EntityType.ITEM,
                labels = emptyMap(),
                descriptions = emptyMap(),
                aliases = emptyMap()
            )
        )

        var capturedTerm: String? = null
        var capturedLanguage: String? = null
        var capturedType: DataModelContract.EntityType? = null
        var capturedLimit: Int? = null
        var capturedPage: Int? = null
        client.wikibase.searchForEntities = { givenSearchTerm, givenLanguage, givenType, givenLimit, givenPageIdx ->
            capturedTerm = givenSearchTerm
            capturedLanguage = givenLanguage
            capturedType = givenType
            capturedLimit = givenLimit
            capturedPage = givenPageIdx

            SuspendingFunctionWrapperStub { entities }
        }

        val mappedEntities = expected.toMutableList()

        val capturedEntities: MutableList<DataModelContract.Entity> = mutableListOf()
        mapper.map = { givenEntity ->
            capturedEntities.add(givenEntity)

            mappedEntities.removeFirst()
        }

        // When
        val actual = RemoteRepository(client, mapper).searchForItem(searchTerm, language)

        // Then
        actual mustBe expected

        capturedEntities mustBe capturedEntities

        capturedTerm mustBe searchTerm
        capturedLanguage mustBe language
        capturedType mustBe DataModelContract.EntityType.ITEM
        capturedLimit mustBe 50
        capturedPage mustBe 0
    }
}
