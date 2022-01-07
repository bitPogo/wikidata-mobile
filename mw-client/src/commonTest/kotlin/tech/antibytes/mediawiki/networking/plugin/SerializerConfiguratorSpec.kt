/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.networking.plugin

import io.ktor.client.features.json.JsonFeature
import kotlinx.serialization.json.JsonBuilder
import tech.antibytes.mock.serialization.JsonConfiguratorStub
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.Test

class SerializerConfiguratorSpec {
    @Test
    fun `It fulfils SerializerConfigurator`() {
        SerializerConfigurator() fulfils KtorPluginsContract.SerializerConfigurator::class
    }

    @Test
    fun `Given configure is called with a JsonFeatureConfig it just runs while configuring the serializer`() {
        // Given
        val pluginConfig = JsonFeature.Config()
        val jsonConfigurator = JsonConfiguratorStub()

        var capturedBuilder: JsonBuilder? = null
        jsonConfigurator.configure = { delegatedBuilder ->
            capturedBuilder = delegatedBuilder
            delegatedBuilder
        }

        // When
        val result = SerializerConfigurator().configure(pluginConfig, jsonConfigurator)

        // Then
        result mustBe Unit
        capturedBuilder!! fulfils JsonBuilder::class
    }
}
