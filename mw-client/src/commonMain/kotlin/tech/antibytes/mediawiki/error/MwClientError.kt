/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.error

import io.ktor.http.HttpStatusCode

sealed class MwClientError(
    message: String? = null,
    cause: Throwable? = null
) : RuntimeException(message, cause) {
    class NoConnection : MwClientError()
    class RequestError(val status: HttpStatusCode) : MwClientError()
    class RequestValidationFailure(message: String) : MwClientError(message)
    class ResponseTransformFailure : MwClientError(message = "Unexpected ResponseType")
    class InternalFailure(message: String) : MwClientError(message)
}
