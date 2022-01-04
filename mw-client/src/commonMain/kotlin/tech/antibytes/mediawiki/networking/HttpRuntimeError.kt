/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.networking

import io.ktor.http.HttpStatusCode

internal sealed class HttpRuntimeError(message: String? = null) : RuntimeException(message) {
    class RequestError(val status: HttpStatusCode) : HttpRuntimeError()
    class NoConnection : HttpRuntimeError()
    class RequestValidationFailure(message: String) : HttpRuntimeError(message)
}
