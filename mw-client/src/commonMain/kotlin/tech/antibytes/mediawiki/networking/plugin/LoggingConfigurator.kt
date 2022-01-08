/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.networking.plugin

import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import tech.antibytes.mediawiki.PublicApi

internal class LoggingConfigurator : KtorPluginsContract.LoggingConfigurator {
    override fun configure(pluginConfiguration: Logging.Config, subConfiguration: PublicApi.Logger) {
        pluginConfiguration.logger = subConfiguration
        pluginConfiguration.level = LogLevel.ALL
    }
}
