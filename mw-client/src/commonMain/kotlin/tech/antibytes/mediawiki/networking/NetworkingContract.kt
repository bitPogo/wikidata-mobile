/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.networking

import io.ktor.client.statement.HttpStatement

internal typealias Header = Map<String, String>
internal typealias Parameter = Map<String, Any?>
internal typealias Path = List<String>

internal interface NetworkingContract {
    enum class Method(val value: String) {
        HEAD("head"),
        DELETE("delete"),
        GET("get"),
        POST("post"),
        PUT("put")
    }

    interface RequestBuilder {
        fun setHeaders(header: Header): RequestBuilder
        fun setParameter(parameter: Parameter): RequestBuilder
        fun useJsonContentType(): RequestBuilder
        fun setBody(body: Any): RequestBuilder

        fun prepare(
            method: Method = Method.GET,
            path: Path = listOf("")
        ): HttpStatement

        companion object {
            val BODYLESS_METHODS = listOf(Method.HEAD, Method.GET)
        }
    }
}
