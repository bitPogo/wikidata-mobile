/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.serialization

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder
import tech.antibytes.mediawiki.networking.plugin.KtorPluginsContract
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class JsonConfiguratorSpec {
    @Test
    fun `It fulfils JsonConfigurator`() {
        JsonConfigurator() fulfils KtorPluginsContract.JsonConfigurator::class
    }

    @Test
    fun `Given configure is called with a JsonBuilder it returns a JsonBuilder`() {
        // Given
        var builder: JsonBuilder? = null
        Json { builder = this }

        // When
        val result: Any = JsonConfigurator().configure(builder!!)

        // Then
        result fulfils JsonBuilder::class
    }

    @Test
    fun `Given configure is called it configures the resulting Json Serializer`() {
        // Given
        var builder: JsonBuilder? = null
        Json { builder = this }

        // When
        val result = JsonConfigurator().configure(builder!!)

        // Then
        result.isLenient mustBe true
        result.ignoreUnknownKeys mustBe true
        result.allowSpecialFloatingPointValues mustBe true
        result.useArrayPolymorphism mustBe false
    }
}
