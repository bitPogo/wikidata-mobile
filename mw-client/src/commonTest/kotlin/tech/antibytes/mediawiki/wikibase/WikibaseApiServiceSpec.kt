/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase

import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpStatement
import tech.antibytes.fixture.wikibase.q42
import tech.antibytes.mediawiki.error.MwClientError
import tech.antibytes.mediawiki.networking.NetworkingContract
import tech.antibytes.mediawiki.networking.Path
import tech.antibytes.mediawiki.wikibase.model.EntityResponse
import tech.antibytes.mock.networking.RequestBuilderStub
import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fixture.listFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.ktor.KtorMockClientFactory
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class WikibaseApiServiceSpec {
    private val fixture = kotlinFixture()
    private val ktorDummy = HttpRequestBuilder()

    @Test
    fun `It fuflfils ApiService`() {
        WikibaseApiService(RequestBuilderStub()) fulfils WikibaseContract.ApiService::class
    }

    @Test
    fun `Given fetchEntity is called with a Set of Ids it fails due to a unexpected response`() = runBlockingTest {
        // Given
        val requestBuilder = RequestBuilderStub()
        val ids = fixture.listFixture<String>().toSet()

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
            WikibaseApiService(requestBuilder).fetchEntity(ids)
        }

        assertEquals(
            actual = error.message,
            expected = "Unexpected Response"
        )
    }

    @Test
    fun `Given fetchEntity is called with a Set of Ids, it returns a EntityResponse`() = runBlockingTest {
        // Given
        val requestBuilder = RequestBuilderStub()
        val ids = fixture.listFixture<String>(size = 2).toSet()

        val expectedResponse = EntityResponse(
            entities = mapOf(
                fixture.fixture<String>() to q42
            ),
            success = fixture.fixture()
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
        val response: EntityResponse = WikibaseApiService(requestBuilder).fetchEntity(ids)

        // Then
        response sameAs expectedResponse
        capturedMethod mustBe NetworkingContract.Method.GET
        capturedPath mustBe listOf("w", "api.php")
        requestBuilder.delegatedParameter mustBe mapOf(
            "action" to "wbgetentities",
            "format" to "json",
            "ids" to ids.joinToString("|")
        )
    }
}
