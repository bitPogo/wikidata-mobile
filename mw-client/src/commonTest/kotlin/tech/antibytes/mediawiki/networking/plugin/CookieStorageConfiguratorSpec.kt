/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.networking.plugin

import io.ktor.client.features.cookies.HttpCookies
import tech.antibytes.mock.networking.plugin.CookieStorageStub
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs
import kotlin.test.Test

class CookieStorageConfiguratorSpec {
    @Test
    fun `It fulfils CookieStorageConfigurator`() {
        CookieStorageConfigurator() fulfils KtorPluginsContract.CookieStorageConfigurator::class
    }

    @Test
    fun `Given configure is called with a HttpCookiesConfig, it sets it up and just runs`() {
        // Given
        val config = HttpCookies.Config()
        val storage = CookieStorageStub()

        // When
        val result = CookieStorageConfigurator().configure(config, storage)

        // Then
        result mustBe Unit
        config.storage sameAs storage
    }
}
