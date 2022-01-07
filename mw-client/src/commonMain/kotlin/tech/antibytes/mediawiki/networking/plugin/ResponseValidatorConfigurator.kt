/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.networking.plugin

import io.ktor.client.features.HttpCallValidator

internal class ResponseValidatorConfigurator :
    KtorPluginsContract.ResponseValidatorConfigurator {
    override fun configure(
        pluginConfiguration: HttpCallValidator.Config,
        subConfiguration: KtorPluginsContract.ErrorMapper?
    ) {
        if (subConfiguration != null) {
            pluginConfiguration.handleResponseException { error ->
                subConfiguration.mapAndThrow(error)
            }
        }
    }
}
