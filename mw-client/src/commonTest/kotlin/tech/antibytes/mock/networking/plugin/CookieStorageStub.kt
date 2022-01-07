/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mock.networking.plugin

import io.ktor.client.features.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.Url
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

class CookieStorageStub(
    var addCookie: ((Url, Cookie) -> Unit)? = null,
    var close: (() -> Unit)? = null,
    var get: ((Url) -> List<Cookie>)? = null

) : CookiesStorage, MockContract.Mock {
    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        return addCookie?.invoke(requestUrl, cookie) ?: throw MockError.MissingStub("Missing sideeffect addCookie")
    }

    override fun close() {
        return close?.invoke() ?: throw MockError.MissingStub("Missing sideeffect close")
    }

    override suspend fun get(requestUrl: Url): List<Cookie> {
        return get?.invoke(requestUrl) ?: throw MockError.MissingStub("Missing sideeffect close")
    }

    override fun clear() {
        addCookie = null
        close = null
        get = null
    }
}
