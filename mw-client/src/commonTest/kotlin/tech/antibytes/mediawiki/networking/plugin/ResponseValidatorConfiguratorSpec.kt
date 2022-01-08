/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.networking.plugin

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.features.HttpCallValidator
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import tech.antibytes.mock.networking.plugin.ErrorMapperStub
import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fulfils
import kotlin.test.Test
import kotlin.test.assertFailsWith

class ResponseValidatorConfiguratorSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils ResponseValidatorConfigurator`() {
        ResponseValidatorConfigurator() fulfils KtorPluginsContract.ResponseValidatorConfigurator::class
    }

    @Test
    fun `Given configure is called  with a Pair of Validators, it configures a ErrorPropagator if it is not null`() = runBlockingTest {
        // Given
        val propagator = ErrorMapperStub()

        propagator.propagate = {
            throw RuntimeException()
        }

        val client = HttpClient(MockEngine) {
            engine {
                addHandler {
                    respond(
                        fixture.fixture<String>(),
                        status = HttpStatusCode.BadRequest
                    )
                }
            }

            install(HttpCallValidator) {
                ResponseValidatorConfigurator().configure(
                    this,
                    propagator
                )
            }
        }

        // When
        assertFailsWith<RuntimeException> {
            client.request<HttpResponse>(fixture.fixture<String>())
        }
    }
}
