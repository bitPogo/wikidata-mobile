/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mock.networking

import io.ktor.client.statement.HttpStatement
import tech.antibytes.mediawiki.networking.Header
import tech.antibytes.mediawiki.networking.NetworkingContract
import tech.antibytes.mediawiki.networking.Parameter
import tech.antibytes.mediawiki.networking.Path
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

internal class RequestBuilderStub(
    var prepare: ((NetworkingContract.Method, Path) -> HttpStatement)? = null
) : NetworkingContract.RequestBuilder, MockContract.Mock {
    private var headers: Header? = null
    val delegatedHeaders: Header?
        get() = headers

    private var parameter: Parameter? = null
    val delegatedParameter: Parameter?
        get() = parameter

    private var body: Any? = null
    val delegatedBody: Any?
        get() = body

    override fun setHeaders(header: Header): NetworkingContract.RequestBuilder {
        return this.also { this.headers = header }
    }

    override fun setParameter(parameter: Parameter): NetworkingContract.RequestBuilder {
        return this.also { this.parameter = parameter }
    }

    override fun setBody(body: Any): NetworkingContract.RequestBuilder {
        return this.also { this.body = body }
    }

    override fun prepare(
        method: NetworkingContract.Method,
        path: Path
    ): HttpStatement {
        return prepare?.invoke(
            method,
            path
        ) ?: throw MockError.MissingStub("Missing Sideeffect prepare-")
    }

    override fun clear() {
        headers = null
        parameter = null
        body = null
    }
}
