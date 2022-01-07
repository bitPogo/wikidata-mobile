/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.networking.plugin

import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.mock.LoggerStub
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.Test

class LoggingConfiguratorTest {
    @Test
    fun `It fulfils LoggingConfigurator`() {
        LoggingConfigurator() fulfils KtorPluginsContract.LoggingConfigurator::class
    }

    @Test
    fun `Given configure is called with a LoggingConfig, it sets it up and just runs`() {
        // Given
        val config = Logging.Config()
        val internalLogger = LoggerStub()
        var wasCalled = false

        internalLogger.log = { _ ->
            wasCalled = true
        }

        // When
        val result = LoggingConfigurator().configure(config, internalLogger)
        config.logger.log("test")

        // Then
        result mustBe Unit
        config.level mustBe LogLevel.ALL

        config.logger fulfils PublicApi.Logger::class
        wasCalled mustBe true
    }
}
