/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.page

import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpStatement
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kfixture.listFixture
import tech.antibytes.kfixture.mapFixture
import tech.antibytes.mediawiki.core.page.model.Page
import tech.antibytes.mediawiki.core.page.model.PageResponse
import tech.antibytes.mediawiki.core.page.model.Query
import tech.antibytes.mediawiki.error.MwClientError
import tech.antibytes.mediawiki.networking.NetworkingContract
import tech.antibytes.mediawiki.networking.Path
import tech.antibytes.mock.networking.RequestBuilderFactoryStub
import tech.antibytes.mock.networking.RequestBuilderStub
import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.ktor.KtorMockClientFactory
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PageApiServiceSpec {
    private val fixture = kotlinFixture()
    private val ktorDummy = HttpRequestBuilder()
    private val requestBuilder = RequestBuilderStub()
    private val requestBuilderFactory = RequestBuilderFactoryStub(requestBuilder)

    @BeforeTest
    fun setUp() {
        requestBuilder.clear()
    }

    @Test
    fun `It fulfils ApiService`() {
        PageApiService(requestBuilderFactory) fulfils PageContract.ApiService::class
    }

    @Test
    fun `Given is randomPage called with a Limit it fals due to a unexpected response`() = runBlockingTest {
        // Given
        val limit: Int = fixture.fixture()

        val client = KtorMockClientFactory.createObjectMockClient { scope, _ ->
            return@createObjectMockClient scope.respond(
                content = fixture.fixture<String>()
            )
        }

        requestBuilder.prepare = { _, _ ->
            HttpStatement(
                ktorDummy,
                client
            )
        }

        // Then
        val error = assertFailsWith<MwClientError.ResponseTransformFailure> {
            // When
            PageApiService(requestBuilderFactory).randomPage(limit)
        }

        assertEquals(
            actual = error.message,
            expected = "Unexpected Response"
        )
    }

    @Test
    fun `Given is randomPage called with a Limit it returns a PageResponse`() = runBlockingTest {
        // Given
        val limit: Int = fixture.fixture()

        val expectedResponse = PageResponse(
            query = Query(
                pages = mapOf(
                    fixture.fixture<String>() to Page(
                        title = fixture.fixture(),
                        revisionId = fixture.fixture()
                    )
                )
            )
        )

        val client = KtorMockClientFactory.createObjectMockClient(listOf(expectedResponse)) { scope, _ ->
            return@createObjectMockClient scope.respond(
                content = fixture.fixture<String>()
            )
        }

        var capturedMethod: NetworkingContract.Method? = null
        var capturedPath: Path? = null

        requestBuilder.prepare = { method, path ->
            capturedMethod = method
            capturedPath = path
            HttpStatement(ktorDummy, client)
        }

        // When
        val response: PageResponse = PageApiService(requestBuilderFactory).randomPage(limit)

        // Then
        response sameAs expectedResponse
        capturedMethod mustBe NetworkingContract.Method.GET
        capturedPath mustBe listOf("w", "api.php")
        requestBuilder.delegatedParameter mustBe mapOf(
            "action" to "query",
            "format" to "json",
            "generator" to "random",
            "prop" to "info",
            "grnlimit" to limit
        )
    }

    @Test
    fun `Given is randomPage called with a Limit and a Namespace it returns a PageResponse`() = runBlockingTest {
        // Given
        val limit: Int = fixture.fixture()
        val namespace: Int = fixture.fixture()

        val expectedResponse = PageResponse(
            query = Query(
                pages = mapOf(
                    fixture.fixture<String>() to Page(
                        title = fixture.fixture(),
                        revisionId = fixture.fixture()
                    )
                )
            )
        )

        val client = KtorMockClientFactory.createObjectMockClient(listOf(expectedResponse)) { scope, _ ->
            return@createObjectMockClient scope.respond(
                content = fixture.fixture<String>()
            )
        }

        var capturedMethod: NetworkingContract.Method? = null
        var capturedPath: Path? = null

        requestBuilder.prepare = { method, path ->
            capturedMethod = method
            capturedPath = path
            HttpStatement(ktorDummy, client)
        }

        // When
        val response: PageResponse = PageApiService(requestBuilderFactory).randomPage(limit, namespace)

        // Then
        response sameAs expectedResponse
        capturedMethod mustBe NetworkingContract.Method.GET
        capturedPath mustBe listOf("w", "api.php")
        requestBuilder.delegatedParameter mustBe mapOf(
            "action" to "query",
            "format" to "json",
            "generator" to "random",
            "prop" to "info",
            "grnlimit" to limit,
            "grnnamespace" to namespace
        )
    }

    @Test
    fun `Given is fetchRestrictions called with a PageTitle it fals due to a unexpected response`() = runBlockingTest {
        // Given
        val title: String = fixture.fixture()

        val client = KtorMockClientFactory.createObjectMockClient { scope, _ ->
            return@createObjectMockClient scope.respond(
                content = fixture.fixture<String>()
            )
        }

        requestBuilder.prepare = { _, _ ->
            HttpStatement(
                ktorDummy,
                client
            )
        }

        // Then
        val error = assertFailsWith<MwClientError.ResponseTransformFailure> {
            // When
            PageApiService(requestBuilderFactory).fetchRestrictions(title)
        }

        assertEquals(
            actual = error.message,
            expected = "Unexpected Response"
        )
    }

    @Test
    fun `Given is fetchRestrictions called with a PageTitle it returns a PageRestrictionResponse`() = runBlockingTest {
        // Given
        val title: String = fixture.fixture()

        val expectedResponse = PageResponse(
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

        val client = KtorMockClientFactory.createObjectMockClient(listOf(expectedResponse)) { scope, _ ->
            return@createObjectMockClient scope.respond(
                content = fixture.fixture<String>()
            )
        }

        var capturedMethod: NetworkingContract.Method? = null
        var capturedPath: Path? = null

        requestBuilder.prepare = { method, path ->
            capturedMethod = method
            capturedPath = path
            HttpStatement(ktorDummy, client)
        }

        // When
        val response: PageResponse = PageApiService(requestBuilderFactory).fetchRestrictions(title)

        // Then
        response sameAs expectedResponse
        capturedMethod mustBe NetworkingContract.Method.GET
        capturedPath mustBe listOf("w", "api.php")
        requestBuilder.delegatedParameter mustBe mapOf(
            "action" to "query",
            "format" to "json",
            "prop" to "info",
            "inprop" to "protection",
            "titles" to title
        )
    }
}
