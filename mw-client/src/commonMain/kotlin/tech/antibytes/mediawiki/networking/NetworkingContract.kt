/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.networking

import io.ktor.client.HttpClientConfig
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.statement.HttpStatement

internal typealias Header = Map<String, String>
internal typealias Parameter = Map<String, Any?>
internal typealias Path = List<String>

internal interface NetworkingContract {
    fun interface PluginConfigurator<PluginConfiguration : Any, SubConfiguration> {
        fun configure(pluginConfiguration: PluginConfiguration, subConfiguration: SubConfiguration)
    }

    data class Plugin<PluginConfiguration : Any, SubConfiguration>(
        val feature: HttpClientFeature<*, *>,
        val pluginConfigurator: PluginConfigurator<PluginConfiguration, SubConfiguration>,
        val subConfiguration: SubConfiguration
    )

    interface ClientConfigurator {
        fun configure(
            httpConfig: HttpClientConfig<*>,
            installers: Set<Plugin<in Any, in Any?>>? = null
        )
    }

    enum class Method(val value: String) {
        HEAD("head"),
        DELETE("delete"),
        GET("get"),
        POST("post"),
        PUT("put")
    }

    enum class KoinIdentifier {
        PLAIN_CLIENT,
        CONFIGURED_CLIENT,
        HOST,
        PORT
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
