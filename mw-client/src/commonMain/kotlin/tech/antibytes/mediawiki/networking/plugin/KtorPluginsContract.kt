/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.networking.plugin

import io.ktor.client.features.HttpCallValidator
import io.ktor.client.features.json.JsonFeature
import kotlinx.serialization.json.JsonBuilder
import tech.antibytes.mediawiki.networking.NetworkingContract

internal interface KtorPluginsContract {
    fun interface ErrorMapper {
        fun mapAndThrow(error: Throwable)
    }

    interface JsonConfiguratorContract {
        fun configure(jsonBuilder: JsonBuilder): JsonBuilder
    }
    fun interface SerializerConfigurator : NetworkingContract.PluginConfigurator<JsonFeature.Config, JsonConfiguratorContract>
    fun interface ResponseValidatorConfigurator : NetworkingContract.PluginConfigurator<HttpCallValidator.Config, ErrorMapper?>
}
