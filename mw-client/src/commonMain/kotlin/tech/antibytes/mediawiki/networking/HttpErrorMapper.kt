/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.networking

import io.ktor.client.features.ResponseException
import tech.antibytes.mediawiki.error.MwClientError
import tech.antibytes.mediawiki.networking.plugin.KtorPluginsContract

internal class HttpErrorMapper : KtorPluginsContract.ErrorMapper {
    private fun wrapError(error: Throwable): Throwable {
        return if (error is ResponseException) {
            MwClientError.RequestError(error.response.status)
        } else {
            error
        }
    }

    override fun mapAndThrow(error: Throwable) {
        throw wrapError(error)
    }
}
