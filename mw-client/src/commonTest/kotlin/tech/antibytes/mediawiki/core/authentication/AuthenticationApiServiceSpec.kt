/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.authentication

import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.statement.HttpStatement
import tech.antibytes.mediawiki.core.authentication.model.ClientLogin
import tech.antibytes.mediawiki.core.authentication.model.LoginResponse
import tech.antibytes.mediawiki.core.authentication.model.LoginStatus
import tech.antibytes.mediawiki.error.MwClientError
import tech.antibytes.mediawiki.networking.NetworkingContract
import tech.antibytes.mediawiki.networking.Path
import tech.antibytes.mock.networking.RequestBuilderStub
import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.ktor.KtorMockClientFactory
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AuthenticationApiServiceSpec {
    private val fixture = kotlinFixture()
    private val ktorDummy = HttpRequestBuilder()

    @Test
    fun `It fulfils ApiService`() {
        AuthenticationApiService(RequestBuilderStub()) fulfils AuthenticationContract.ApiService::class
    }

    @Test
    fun `Given login is called with a UserName, Password and a Token it fails due to unexpected response`() = runBlockingTest {
        // Given
        val requestBuilder = RequestBuilderStub()
        val username: String = fixture.fixture()
        val password: String = fixture.fixture()
        val token: String = fixture.fixture()

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
            AuthenticationApiService(requestBuilder).login(username, password, token)
        }

        assertEquals(
            actual = error.message,
            expected = "Unexpected Response"
        )
    }

    @Test
    fun `Given login is called with a UserName, Password and a Token, it returns a LoginResponse`() = runBlockingTest {
        // Given
        val requestBuilder = RequestBuilderStub()
        val username: String = fixture.fixture()
        val password: String = fixture.fixture()
        val token: String = fixture.fixture()

        val status = LoginStatus.PASS

        val expectedResponse = LoginResponse(
            ClientLogin(
                status = status
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
        val response: LoginResponse = AuthenticationApiService(requestBuilder).login(username, password, token)

        // Then
        response sameAs expectedResponse
        capturedMethod mustBe NetworkingContract.Method.POST
        capturedPath mustBe listOf("w", "api.php")
        requestBuilder.delegatedParameter mustBe mapOf(
            "action" to "clientlogin",
            "rememberme" to "",
            "format" to "json",
        )
        requestBuilder.delegatedBody!! fulfils FormDataContent::class
        (requestBuilder.delegatedBody as FormDataContent).formData["logintoken"] mustBe token
        (requestBuilder.delegatedBody as FormDataContent).formData["username"] mustBe username
        (requestBuilder.delegatedBody as FormDataContent).formData["password"] mustBe password
        (requestBuilder.delegatedBody as FormDataContent).formData["loginreturnurl"] mustBe "https://www.wikidata.org/wiki/Lexeme:L52317"
    }
}
