/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.page.transfer.repository

import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fixture.listFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.isNot
import tech.antibytes.util.test.mustBe
import tech.antibytes.wikibase.store.mock.database.PageQueriesStub
import tech.antibytes.wikibase.store.mock.database.QueryStub
import tech.antibytes.wikibase.store.mock.database.SqlCursorStub
import tech.antibytes.wikibase.store.page.domain.DomainContract
import tech.antibytes.wikibase.store.page.domain.model.EntityId
import kotlin.test.BeforeTest
import kotlin.test.Test

class LocalRepositorySpec {
    private val fixture = kotlinFixture()
    private val database = PageQueriesStub()

    @BeforeTest
    fun setUp() {
        database.clear()
    }

    @Test
    fun `It fulfils LocalRepository`() {
        LocalRepository(database) fulfils DomainContract.LocalRepository::class
    }

    @Test
    fun `Given fetchRandomPageId is called it returns null if no id is stored`() {
        // Given

        val peekRandomPageCursor = SqlCursorStub { false }

        database.peekRandomPages = {
            QueryStub(
                mapper = { fixture.fixture() },
                execute = { peekRandomPageCursor }
            )
        }

        // When
        val actual = LocalRepository(database).fetchRandomPageId()

        // Then
        actual mustBe null
    }

    @Test
    fun `Given fetchRandomPageId is called it returns the stored id, while deleting it from the database`() {
        // Given
        val id: String = fixture.fixture()
        val nexts = mutableListOf(true, false)
        val peekRandomPageCursor = SqlCursorStub { nexts.removeFirst() }

        database.peekRandomPages = {
            QueryStub(
                mapper = { id },
                execute = { peekRandomPageCursor }
            )
        }

        var capturedId: String? = null
        database.deleteRandomPage = { givenId ->
            capturedId = givenId
        }

        // When
        val actual = LocalRepository(database).fetchRandomPageId()

        // Then
        actual mustBe id
        capturedId mustBe id
    }

    @Test
    fun `Given saveRandomPageIds with a List of EntityIds, it does nothing if the List was empty`() {
        // Given
        val ids = emptyList<EntityId>()

        // When
        val result = LocalRepository(database).saveRandomPageIds(ids)

        // Then
        result mustBe Unit
    }

    @Test
    fun `Given saveRandomPageIds with a List of EntityIds, it saves the List transaction wise`() {
        // Given
        val ids = fixture.listFixture<String>(size = 5)

        val capturedIds: MutableList<EntityId> = mutableListOf()
        database.insertRandomPage = { id ->
            capturedIds.add(id)
        }

        // When
        val result = LocalRepository(database).saveRandomPageIds(ids)

        // Then
        result mustBe Unit
        capturedIds mustBe ids
        database.lastTransaction isNot null
    }
}
