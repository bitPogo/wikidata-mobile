/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase

import tech.antibytes.fixture.wikibase.q42
import tech.antibytes.mediawiki.EntityId
import tech.antibytes.mock.wikibase.WikibaseRepositoryStub
import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fixture.listFixture
import tech.antibytes.util.test.fulfils
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
    fun `Given fetchEntities is called with a Set of Ids, it delegates the call to the Repository and returns its result`() = runBlockingTest {
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
}
