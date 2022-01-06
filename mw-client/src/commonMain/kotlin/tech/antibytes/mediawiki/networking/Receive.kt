/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.networking

import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.statement.HttpStatement
import tech.antibytes.mediawiki.error.MwClientError

internal suspend inline fun <reified T> receive(request: HttpStatement): T {
    return try {
        request.receive()
    } catch (exception: NoTransformationFoundException) {
        throw MwClientError.ResponseTransformFailure()
    }
}
