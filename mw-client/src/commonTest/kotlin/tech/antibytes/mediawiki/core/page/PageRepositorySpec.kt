/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.page

import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kfixture.listFixture
import tech.antibytes.kfixture.mapFixture
import tech.antibytes.mediawiki.core.page.model.Page
import tech.antibytes.mediawiki.core.page.model.PageResponse
import tech.antibytes.mediawiki.core.page.model.Query
import tech.antibytes.mock.core.page.PageApiServiceStub
import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.BeforeTest
import kotlin.test.Test

class PageRepositorySpec {
    private val fixture = kotlinFixture()
    private val apiService = PageApiServiceStub()

    @BeforeTest
    fun setUp() {
        apiService.clear()
    }

    @Test
    fun `It fulfils Repository`() {
        PageRepository(apiService) fulfils PageContract.Repository::class
    }

    @Test
    fun `Given randomPage is called with a Limit and a Namespace it delegates the Call to the ApiService and extracts a List of RevisionedPagePointer`() = runBlockingTest {
        // Given
        val limit: Int = fixture.fixture()
        val namespace: Int = fixture.fixture()

        var capturedLimit: Int? = null
        var capturedNamespace: Int? = null

        val response = PageResponse(
            query = Query(
                pages = mapOf(
                    fixture.fixture<String>() to Page(
                        title = fixture.fixture(),
                        revisionId = fixture.fixture()
                    ),
                    fixture.fixture<String>() to Page(
                        title = fixture.fixture(),
                        revisionId = fixture.fixture()
                    )
                )
            )
        )

        apiService.randomPage = { givenLimit, givenNamespace ->
            capturedLimit = givenLimit
            capturedNamespace = givenNamespace

            response
        }

        // When
        val result = PageRepository(apiService).randomPage(limit, namespace)

        // Then
        result mustBe response.query.pages.values.toList()
        capturedLimit mustBe limit
        capturedNamespace mustBe namespace
    }

    @Test
    fun `Given fetchRestrictions is called with a PageTitle it delegates the Call to the ApiService and extracts a List of Restrictions, if ProtectionLevels are present`() = runBlockingTest {
        // Given
        val title: String = fixture.fixture()

        var capturedTitle: String? = null

        val response = PageResponse(
            query = Query(
                pages = mapOf(
                    fixture.fixture<String>() to Page(
                        fixture.fixture(),
                        fixture.fixture(),
                        fixture.listFixture(),
                        listOf(fixture.mapFixture())
                    )
                )
            )
        )

        apiService.fetchRestrictions = { givenTitle ->
            capturedTitle = givenTitle

            response
        }

        // When
        val result = PageRepository(apiService).fetchRestrictions(title)

        // Then
        result mustBe response.query.pages.values.toList().first().restrictions
        capturedTitle mustBe title
    }

    @Test
    fun `Given fetchRestrictions is called with a PageTitle it delegates the Call to the ApiService and returns a EmptyList, if ProtectionLevels are not present`() = runBlockingTest {
        // Given
        val title: String = fixture.fixture()

        var capturedTitle: String? = null

        val response = PageResponse(
            query = Query(
                pages = mapOf(
                    fixture.fixture<String>() to Page(
                        fixture.fixture(),
                        fixture.fixture(),
                        fixture.listFixture(),
                        emptyList()
                    )
                )
            )
        )

        apiService.fetchRestrictions = { givenTitle ->
            capturedTitle = givenTitle

            response
        }

        // When
        val result = PageRepository(apiService).fetchRestrictions(title)

        // Then
        result mustBe emptyList()
        capturedTitle mustBe title
    }

    @Test
    fun `Given fetchRestrictions is called with a PageTitle it delegates the Call to the ApiService, while removing unwanted seperators`() = runBlockingTest {
        // Given
        val title = "${fixture.fixture<String>()}|${fixture.fixture<String>()}"

        var capturedTitle: String? = null

        val response = PageResponse(
            query = Query(
                pages = mapOf(
                    fixture.fixture<String>() to Page(
                        fixture.fixture(),
                        fixture.fixture(),
                        fixture.listFixture(),
                        emptyList()
                    )
                )
            )
        )

        apiService.fetchRestrictions = { givenTitle ->
            capturedTitle = givenTitle

            response
        }

        // When
        PageRepository(apiService).fetchRestrictions(title)

        // Then
        capturedTitle mustBe title.replace("|", "")
    }
}
