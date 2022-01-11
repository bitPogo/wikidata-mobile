/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.serialization

import kotlinx.serialization.json.JsonBuilder
import tech.antibytes.mediawiki.networking.plugin.KtorPluginsContract

internal class JsonConfigurator : KtorPluginsContract.JsonConfigurator {
    override fun configure(jsonBuilder: JsonBuilder): JsonBuilder {
        jsonBuilder.isLenient = true
        jsonBuilder.ignoreUnknownKeys = true
        jsonBuilder.allowSpecialFloatingPointValues = true
        jsonBuilder.useArrayPolymorphism = false

        return jsonBuilder
    }
}
