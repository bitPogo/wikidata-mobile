/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.networking.plugin

import io.ktor.client.features.cookies.CookiesStorage
import io.ktor.client.features.cookies.HttpCookies

internal class CookieStorageConfigurator : KtorPluginsContract.CookieStorageConfigurator {
    override fun configure(pluginConfiguration: HttpCookies.Config, subConfiguration: CookiesStorage) {
        pluginConfiguration.storage = subConfiguration
    }
}
