/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.page

import tech.antibytes.mediawiki.core.page.model.Page
import tech.antibytes.mock.core.page.PageRepositoryStub
import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fixture.listFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs
import kotlin.test.BeforeTest
import kotlin.test.Test

class PageServiceSpec {
    private val fixture = kotlinFixture()
    private val repository = PageRepositoryStub()

    @BeforeTest
    fun setUp() {
        repository.randomPage = null
    }

    @Test
    fun `It fulfils Service`() {
        PageService(repository) fulfils PageContract.Service::class
    }

    @Test
    fun `Given randomPage is called with a Limit and a Namespace it delegates the call to its repository and returns its result`() = runBlockingTest {
        // Given
        val limit: Int = fixture.fixture()
        val namespace: Int = fixture.fixture()

        val response = listOf(
            Page(
                title = fixture.fixture(),
                revisionId = fixture.fixture()
            ),
            Page(
                title = fixture.fixture(),
                revisionId = fixture.fixture()
            )
        )

        var capturedLimit: Int? = null
        var capturedNamespace: Int? = null

        repository.randomPage = { givenLimit, givenNamespace ->
            capturedLimit = givenLimit
            capturedNamespace = givenNamespace

            response
        }

        // When
        val result = PageService(repository).randomPage(limit, namespace)

        // Then
        result sameAs response
        capturedLimit mustBe limit
        capturedNamespace mustBe namespace
    }

    @Test
    fun `Given fetchRestrictions is called with a PageTitle it delegates the call to its repository and returns its result`() = runBlockingTest {
        // Given
        val title: String = fixture.fixture()

        val response = fixture.listFixture<String>()

        var capturedTitle: String? = null

        repository.fetchRestrictions = { givenTitle ->
            capturedTitle = givenTitle

            response
        }

        // When
        val result = PageService(repository).fetchRestrictions(title)

        // Then
        result sameAs response
        capturedTitle mustBe title
    }
}
