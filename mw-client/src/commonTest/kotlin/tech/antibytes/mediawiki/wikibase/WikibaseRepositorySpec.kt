/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase

import tech.antibytes.fixture.wikibase.q42
import tech.antibytes.mediawiki.EntityId
import tech.antibytes.mediawiki.wikibase.model.EntityResponse
import tech.antibytes.mock.wikibase.WikibaseApiServiceStub
import tech.antibytes.util.test.coroutine.runBlockingTest
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
        WikibaseRepository(apiService) fulfils WikibaseContract.Repository::class
    }

    @Test
    fun `Given fetchEntities is called with a Set of Ids, it delegates the Ids to the ApiService returns a EmptyList if the call was not a Success`() = runBlockingTest {
        // Given
        val ids = fixture.listFixture<EntityId>().toSet()
        val response = EntityResponse(
            entities = emptyMap(),
            success = 0
        )

        var capturedIds: Set<EntityId>? = null

        apiService.fetchEntities = { givenIds ->
            capturedIds = givenIds
            response
        }

        // When
        val result = WikibaseRepository(apiService).fetchEntities(ids)

        // Then
        result mustBe emptyList()
        capturedIds mustBe ids
    }

    @Test
    fun `Given fetchEntities is called with a Set of Ids, it delegates the Ids to the ApiService returns a List of Entities if the call was a Success`() = runBlockingTest {
        // Given
        val ids = fixture.listFixture<EntityId>(size = 2)
        val response = EntityResponse(
            entities = mapOf(
                ids[0] to q42,
                ids[1] to q42
            ),
            success = 1
        )

        var capturedIds: Set<EntityId>? = null

        apiService.fetchEntities = { givenIds ->
            capturedIds = givenIds
            response
        }

        // When
        val result = WikibaseRepository(apiService).fetchEntities(ids.toSet())

        // Then
        result mustBe listOf(q42, q42)
        capturedIds mustBe ids.toSet()
    }
}
