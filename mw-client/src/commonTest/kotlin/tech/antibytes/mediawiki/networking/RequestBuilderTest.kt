/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.networking

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.toByteReadPacket
import io.ktor.client.request.HttpRequestData
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.URLProtocol
import io.ktor.http.fullPath
import io.ktor.http.headersOf
import io.ktor.util.toMap
import kotlinx.coroutines.GlobalScope
import tech.antibytes.mediawiki.error.MwClientError
import tech.antibytes.util.test.coroutine.runBlockingTestWithContext
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fixture.listFixture
import tech.antibytes.util.test.fixture.pairFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.ktor.KtorMockClientFactory
import tech.antibytes.util.test.mustBe
import kotlin.math.absoluteValue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class RequestBuilderTest {
    private val fixture = kotlinFixture()
    private val host: String = fixture.fixture()

    private fun createMockClientWithAssertion(assert: (HttpRequestData) -> Unit): HttpClient {
        return HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    assert.invoke(request)
                    respond(
                        fixture.fixture<String>(),
                        headers = headersOf(
                            "Content-Type" to listOf(
                                ContentType.Text.Plain.toString()
                            )
                        )
                    )
                }
            }
        }
    }

    @Test
    fun `It fulfils RequestBuilder`() {
        // Given
        val client = KtorMockClientFactory.createSimpleMockClient(fixture.fixture())

        // When
        val builder: Any = RequestBuilder(
            client,
            fixture.fixture(),
        )

        // Then
        builder fulfils NetworkingContract.RequestBuilder::class
    }

    @Test
    fun `Given a Request was prepared and executed it uses GET by default`() = runBlockingTestWithContext(GlobalScope.coroutineContext) {
        // Given
        val client = createMockClientWithAssertion { request ->
            // Then
            request.method mustBe HttpMethod.Get
        }

        // When
        RequestBuilder(
            client,
            fixture.fixture(),
        ).prepare().execute()
    }

    @Test
    fun `Given a Request was prepared and executed it calls the given Host`() = runBlockingTestWithContext(GlobalScope.coroutineContext) {
        // Given
        val host: String = fixture.fixture()
        val client = createMockClientWithAssertion { request ->
            // Then
            request.url.host mustBe host
        }

        // When
        RequestBuilder(
            client,
            host,
        ).prepare().execute()
    }

    @Test
    fun `Given a Request was prepared and executed it calls the root by default`() = runBlockingTestWithContext(GlobalScope.coroutineContext) {
        // Given
        val client = createMockClientWithAssertion { request ->
            // Then
            request.url.fullPath mustBe "/"
        }

        // When
        RequestBuilder(
            client,
            host,
        ).prepare().execute()
    }

    @Test
    fun `Given a Request was prepared and executed with a Path it calls the given path`() = runBlockingTestWithContext(GlobalScope.coroutineContext) {
        // Given
        val path = fixture.listFixture<String>(size = 3)

        val host: String = fixture.fixture()
        val client = createMockClientWithAssertion { request ->
            // Then
            request.url.fullPath mustBe "/${path.joinToString("/")}"
        }

        // When
        RequestBuilder(
            client,
            host,
        ).prepare(path = path).execute()
    }

    @Test
    fun `Given a Request was executed it calls the given Host via HTTPS`() = runBlockingTestWithContext(GlobalScope.coroutineContext) {
        // Given
        val client = createMockClientWithAssertion { request ->
            // Then
            request.url.protocol mustBe URLProtocol.HTTPS
        }

        // When
        RequestBuilder(
            client,
            host,
        ).prepare().execute()
    }

    @Test
    fun `Given a Request was executed it calls the given Host and HTTP if instructed to`() = runBlockingTestWithContext(GlobalScope.coroutineContext) {
        // Given
        val client = createMockClientWithAssertion { request ->
            // Then
            request.url.protocol mustBe URLProtocol.HTTP
        }

        // When
        RequestBuilder(
            client,
            host,
            protocol = URLProtocol.HTTP
        ).prepare().execute()
    }

    @Test
    fun `Given a Request was prepared and executed it uses the default Port`() = runBlockingTestWithContext(GlobalScope.coroutineContext) {
        // Given
        val client = createMockClientWithAssertion { request ->
            // Then
            request.url.port mustBe URLProtocol.HTTPS.defaultPort
        }

        // When
        RequestBuilder(
            client,
            host,
        ).prepare().execute()
    }

    @Test
    fun `Given a instance was create with a Environment and a Port and it was prepared and executed it uses the given Port`() = runBlockingTestWithContext(GlobalScope.coroutineContext) {
        // Given
        val port = fixture.fixture<Short>().toInt().absoluteValue
        val host: String = fixture.fixture()

        val client = createMockClientWithAssertion { request ->
            // Then
            request.url.port mustBe port
        }

        // When
        RequestBuilder(
            client,
            host,
            port = port
        ).prepare().execute()
    }

    @Test
    fun `Given a Request was prepared and executed it sets no custom headers to the request by default`() = runBlockingTestWithContext(GlobalScope.coroutineContext) {
        // Given
        val client = createMockClientWithAssertion { request ->
            // Then
            request.headers.toMap() mustBe mapOf(
                "Accept-Charset" to listOf("UTF-8"),
                "Accept" to listOf("*/*")
            )
        }

        // When
        RequestBuilder(
            client,
            host,
        ).prepare().execute()
    }

    @Test
    fun `Given a instance was create with a Environment, setHeaders was called with Headers and it was prepared and executed itsets the given headers to the request`() = runBlockingTestWithContext(GlobalScope.coroutineContext) {
        // Given
        val headers = mapOf<String, String>(
            fixture.pairFixture(),
            fixture.pairFixture()
        )

        val keys = headers.keys.toList()
        val host: String = fixture.fixture()
        val client = createMockClientWithAssertion { request ->
            // Then
            request.headers.toMap() mustBe mapOf(
                "Accept-Charset" to listOf("UTF-8"),
                "Accept" to listOf("*/*"),
                keys[0] to listOf(headers[keys[0]]),
                keys[1] to listOf(headers[keys[1]])
            )
        }

        // When
        RequestBuilder(
            client,
            host,
        ).setHeaders(headers).prepare().execute()
    }

    @Test
    fun `Given a Request was prepared and executed itsets no custom parameter to the request by default`() = runBlockingTestWithContext(GlobalScope.coroutineContext) {
        // Given
        val client = createMockClientWithAssertion { request ->
            // Then
            request.url.parameters.toMap() mustBe emptyMap()
        }

        // When
        RequestBuilder(
            client,
            host,
        ).prepare().execute()
    }

    @Test
    fun `Given a instance was create with a Environment, setParameter was called with parameter and it was prepared and executed itsets custom parameter to the request`() = runBlockingTestWithContext(GlobalScope.coroutineContext) {
        // Given
        val parameter = mapOf<String, String>(
            fixture.pairFixture(),
            fixture.pairFixture()
        )

        val keys = parameter.keys.toList()

        val host: String = fixture.fixture()
        val client = createMockClientWithAssertion { request ->
            // Then
            request.url.parameters.toMap() mustBe mapOf(
                keys[0] to listOf(parameter[keys[0]]),
                keys[1] to listOf(parameter[keys[1]])
            )
        }

        // When
        RequestBuilder(
            client,
            host,
        ).setParameter(parameter).prepare().execute()
    }

    @Test
    fun `Given a Request was prepared and executed ithas no Body by default`() = runBlockingTestWithContext(GlobalScope.coroutineContext) {
        // Given
        val client = createMockClientWithAssertion { request ->
            // Then
            request.body.contentLength!! mustBe 0
        }

        // When
        RequestBuilder(
            client,
            host,
        ).prepare().execute()
    }

    @Test
    fun `Given a Requests setBody is called with a Payload and it was prepared and executed with GET, it fails`() = runBlockingTestWithContext(GlobalScope.coroutineContext) {
        // Given
        val client = KtorMockClientFactory.createSimpleMockClient(fixture.fixture())

        // Then
        val error = assertFailsWith<MwClientError.RequestValidationFailure> {
            // When
            RequestBuilder(
                client,
                host,
            ).setBody(fixture.fixture<String>()).prepare(NetworkingContract.Method.GET)
        }

        // Then
        error.message!! mustBe "GET cannot be combined with a RequestBody."
    }

    @Test
    fun `Given a Requests setBody is called with a Payload and it was prepared and executed with HEAD, it fails`() = runBlockingTestWithContext(GlobalScope.coroutineContext) {
        // Given
        val client = KtorMockClientFactory.createSimpleMockClient(fixture.fixture())

        // Then
        val error = assertFailsWith<MwClientError.RequestValidationFailure> {
            // When
            RequestBuilder(
                client,
                host,
            ).setBody(fixture.fixture<String>()).prepare(NetworkingContract.Method.HEAD)
        }

        // Then
        error.message!! mustBe "HEAD cannot be combined with a RequestBody."
    }

    @Test
    fun `Given a Requests setBody was not called and it was prepared and executed with POST, it fails`() = runBlockingTestWithContext(GlobalScope.coroutineContext) {
        // Given
        val client = KtorMockClientFactory.createSimpleMockClient(fixture.fixture())

        // Then
        val error = assertFailsWith<MwClientError.RequestValidationFailure> {
            // When
            RequestBuilder(
                client,
                host,
            ).prepare(NetworkingContract.Method.POST)
        }

        // Then
        error.message!! mustBe "POST must be combined with a RequestBody."
    }

    @Test
    fun `Given a Requests setBody was not called and it was prepared and executed with PUT, it fails`() = runBlockingTestWithContext(GlobalScope.coroutineContext) {
        // Given
        val client = KtorMockClientFactory.createSimpleMockClient(fixture.fixture())

        // Then
        val error = assertFailsWith<MwClientError.RequestValidationFailure> {
            // When
            RequestBuilder(
                client,
                host,
            ).prepare(NetworkingContract.Method.PUT)
        }

        // Then
        error.message!! mustBe "PUT must be combined with a RequestBody."
    }

    @Test
    fun `Given Requests setBody was not called and it was prepared and executed with DELETE, it fails`() = runBlockingTestWithContext(GlobalScope.coroutineContext) {
        // Given
        val client = KtorMockClientFactory.createSimpleMockClient(fixture.fixture())

        // Then
        val error = assertFailsWith<MwClientError.RequestValidationFailure> {
            // When
            RequestBuilder(
                client,
                host,
            ).prepare(NetworkingContract.Method.DELETE)
        }

        // Then
        error.message!! mustBe "DELETE must be combined with a RequestBody."
    }

    @Test
    fun `Given a Request was prepared and executed with HEAD it uses head`() = runBlockingTestWithContext(GlobalScope.coroutineContext) {
        // Given
        val client = createMockClientWithAssertion { request ->
            // Then
            request.method mustBe HttpMethod.Head
        }

        // When
        RequestBuilder(
            client,
            host,
        ).prepare(NetworkingContract.Method.HEAD).execute()
    }

    @Test
    fun `Given a Requests setBody was called with a Payload and it was prepared and executed with POST it uses post`() = runBlockingTestWithContext(GlobalScope.coroutineContext) {
        // Given
        val payload: String = fixture.fixture()

        val client = createMockClientWithAssertion { request ->
            // Then
            request.method mustBe HttpMethod.Post
        }

        // When
        RequestBuilder(
            client,
            host,
        ).setBody(payload).prepare(NetworkingContract.Method.POST).execute()
    }

    @Test
    fun `Given a Requests setBody was called with a Payload and it was prepared and executed with POST it attaches the body to the request`() = runBlockingTestWithContext(GlobalScope.coroutineContext) {
        // Given
        val payload: String = fixture.fixture()

        val client = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    // Then
                    request.body.toByteReadPacket().readText() mustBe payload
                    respond(fixture.fixture<String>())
                }
            }
        }

        // When
        RequestBuilder(
            client,
            host,
        ).setBody(payload).prepare(NetworkingContract.Method.POST).execute()
    }

    @Test
    fun `Given a Requests setBody was called with a Payload and it was prepared and executed with PUT it uses put`() = runBlockingTestWithContext(GlobalScope.coroutineContext) {
        // Given
        val payload: String = fixture.fixture()

        val client = createMockClientWithAssertion { request ->
            // Then
            request.method mustBe HttpMethod.Put
        }

        // When
        RequestBuilder(
            client,
            host,
        ).setBody(payload).prepare(NetworkingContract.Method.PUT).execute()
    }

    @Test
    fun `Given a Requests setBody was called with a Payload and it was prepared and executed with PUT it attaches the body to the request`() = runBlockingTestWithContext(GlobalScope.coroutineContext) {
        // Given
        val payload: String = fixture.fixture()

        val client = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    // Then
                    request.body.toByteReadPacket().readText() mustBe payload
                    respond(fixture.fixture<String>())
                }
            }
        }

        // When
        RequestBuilder(
            client,
            host,
        ).setBody(payload).prepare(NetworkingContract.Method.PUT).execute()
    }

    @Test
    fun `Given a Requests setBody was called with a Payload and it was prepared and executed with DELETE it uses delete`() = runBlockingTestWithContext(GlobalScope.coroutineContext) {
        // Given
        val payload: String = fixture.fixture()

        val client = createMockClientWithAssertion { request ->
            // Then
            request.method mustBe HttpMethod.Delete
        }

        // When
        RequestBuilder(
            client,
            host,
        ).setBody(payload).prepare(NetworkingContract.Method.DELETE).execute()
    }

    @Test
    fun `Given a instance was create with a Environment, setBody was called with a Payload and it was prepared and executed with DELETE it attaches the body to the request`() = runBlockingTestWithContext(GlobalScope.coroutineContext) {
        // Given
        val payload: String = fixture.fixture()

        val client = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    // Then
                    request.body.toByteReadPacket().readText() mustBe payload

                    respond(fixture.fixture<String>())
                }
            }
        }

        // When
        RequestBuilder(
            client,
            host,
        ).setBody(payload).prepare(NetworkingContract.Method.DELETE).execute()
    }
}
