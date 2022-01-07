/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.networking.plugin

import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import kotlinx.serialization.json.Json

internal class SerializerConfigurator : KtorPluginsContract.SerializerConfigurator {
    override fun configure(
        pluginConfiguration: JsonFeature.Config,
        subConfiguration: KtorPluginsContract.JsonConfiguratorContract
    ) {
        pluginConfiguration.serializer = KotlinxSerializer(
            Json { subConfiguration.configure(this) }
        )
    }
}
