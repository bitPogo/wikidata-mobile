/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.token

import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpStatement
import tech.antibytes.mediawiki.core.token.model.MetaTokenResponse
import tech.antibytes.mediawiki.core.token.model.Query
import tech.antibytes.mediawiki.error.MwClientError
import tech.antibytes.mediawiki.networking.NetworkingContract
import tech.antibytes.mediawiki.networking.Path
import tech.antibytes.mock.networking.RequestBuilderFactoryStub
import tech.antibytes.mock.networking.RequestBuilderStub
import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.ktor.KtorMockClientFactory.createObjectMockClient
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MetaTokenApiServiceSpec {
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
        MetaTokenApiService(requestBuilderFactory) fulfils MetaTokenContract.ApiService::class
    }

    @Test
    fun `Given fetchToken was called with a TokenType it fails due to unexpected response`() = runBlockingTest {
        // Given
        val client = createObjectMockClient { scope, _ ->
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
            MetaTokenApiService(requestBuilderFactory).fetchToken(MetaTokenContract.MetaTokenType.CSRF)
        }

        assertEquals(
            actual = error.message,
            expected = "Unexpected Response"
        )
    }

    @Test
    fun `Given fetchToken is called with a TokenType, it returns a TokenResponse`() = runBlockingTest {
        // Given
        val type = MetaTokenContract.MetaTokenType.CSRF
        val tokenResponse = MetaTokenResponse(
            query = Query(
                mapOf(
                    type to fixture.fixture()
                )
            )
        )
        val client = createObjectMockClient(listOf(tokenResponse)) { scope, _ ->
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
        val response = MetaTokenApiService(requestBuilderFactory).fetchToken(type)

        // Then
        response sameAs tokenResponse
        capturedMethod mustBe NetworkingContract.Method.GET
        capturedPath mustBe listOf("w", "api.php")
        requestBuilder.delegatedParameter mustBe mapOf(
            "action" to "query",
            "meta" to "tokens",
            "format" to "json",
            "type" to type.value.removeSuffix("token")
        )
    }
}
