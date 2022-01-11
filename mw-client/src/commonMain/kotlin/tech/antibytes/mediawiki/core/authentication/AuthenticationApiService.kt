/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.authentication

import io.ktor.client.request.forms.FormDataContent
import io.ktor.http.Parameters
import tech.antibytes.mediawiki.MwClientContract.Companion.ENDPOINT
import tech.antibytes.mediawiki.core.authentication.model.LoginResponse
import tech.antibytes.mediawiki.core.token.MetaToken
import tech.antibytes.mediawiki.networking.NetworkingContract
import tech.antibytes.mediawiki.networking.receive

internal class AuthenticationApiService(
    private val requestBuilder: NetworkingContract.RequestBuilderFactory
) : AuthenticationContract.ApiService {
    private fun createPayload(
        username: String,
        password: String,
        token: MetaToken
    ): FormDataContent {
        return FormDataContent(
            Parameters.build {
                append("logintoken", token)
                append("username", username)
                append("password", password)
                append("loginreturnurl", "https://www.wikidata.org/wiki/Lexeme:L52317")
            }
        )
    }

    override suspend fun login(
        username: String,
        password: String,
        token: MetaToken
    ): LoginResponse {
        val payload = createPayload(username, password, token)

        val request = requestBuilder.create()
            .setParameter(PARAMETER)
            .setBody(payload)
            .prepare(
                NetworkingContract.Method.POST,
                ENDPOINT
            )

        return receive(request)
    }

    private companion object {
        val PARAMETER = mapOf(
            "action" to "clientlogin",
            "format" to "json",
        )
    }
}
