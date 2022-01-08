/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.networking.plugin

import io.ktor.client.features.HttpCallValidator
import io.ktor.client.features.cookies.CookiesStorage
import io.ktor.client.features.cookies.HttpCookies
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.Logging
import kotlinx.serialization.json.JsonBuilder
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.mediawiki.networking.NetworkingContract

internal interface KtorPluginsContract {
    fun interface ErrorMapper {
        fun mapAndThrow(error: Throwable)
    }

    interface JsonConfigurator {
        fun configure(jsonBuilder: JsonBuilder): JsonBuilder
    }

    fun interface LoggingConfigurator : NetworkingContract.PluginConfigurator<Logging.Config, PublicApi.Logger>
    fun interface SerializerConfigurator : NetworkingContract.PluginConfigurator<JsonFeature.Config, JsonConfigurator>
    fun interface ResponseValidatorConfigurator : NetworkingContract.PluginConfigurator<HttpCallValidator.Config, ErrorMapper>
    fun interface CookieStorageConfigurator : NetworkingContract.PluginConfigurator<HttpCookies.Config, CookiesStorage>
}
