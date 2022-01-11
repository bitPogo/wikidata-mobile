/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.networking

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respondError
import io.ktor.client.features.HttpResponseValidator
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import tech.antibytes.mediawiki.error.MwClientError
import tech.antibytes.mediawiki.networking.plugin.KtorPluginsContract
import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs
import kotlin.test.Test
import kotlin.test.assertFailsWith

class HttpErrorMapperSpec {
    @Test
    fun `It fulfils ErrorMapper`() {
        HttpErrorMapper() fulfils KtorPluginsContract.ErrorMapper::class
    }

    @Test
    fun `Given mapAndThrow is called with a Throwable, it rethrows non ResponseException`() {
        // Given
        val throwable = RuntimeException("abc")

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            HttpErrorMapper().mapAndThrow(throwable)
        }

        // Then
        error sameAs throwable
    }

    @Test
    fun `Given mapAndThrow is called with a Throwable, it rethrows it as RequestError, which contains a HttpStatusCode`() = runBlockingTest {
        // Given
        val client = HttpClient(MockEngine) {
            HttpResponseValidator {
                handleResponseException { response ->
                    HttpErrorMapper().mapAndThrow(response)
                }
            }

            engine {
                addHandler {
                    respondError(
                        status = HttpStatusCode.InternalServerError
                    )
                }
            }
        }

        // Then
        val error = assertFailsWith<MwClientError.RequestError> {
            client.get("/somewhre")
        }

        error.status mustBe HttpStatusCode.InternalServerError
    }
}
