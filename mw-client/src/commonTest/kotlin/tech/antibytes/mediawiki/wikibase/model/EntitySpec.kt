/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase.model

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import tech.antibytes.fixture.wikibase.q42
import tech.antibytes.mock.ResourceLoader
import tech.antibytes.util.test.mustBe
import kotlin.test.Test

class EntitySpec {
    private val serializer = Json {
        serializersModule = SerializersModule {
            ignoreUnknownKeys = true
        }
    }

    @Test
    fun `It decodes a Entity`() {
        // Given
        val serializedEntity = ResourceLoader.loader.load("/fixture/wikibase/Q42.json")

        // When
        val result = serializer.decodeFromString(
            Entity.serializer(),
            serializedEntity
        )

        // Then
        result.id mustBe q42.id
        result.revision mustBe q42.revision
        result.lastModification mustBe q42.lastModification
        result.type mustBe q42.type
        result.labels mustBe q42.labels
        result.descriptions mustBe q42.descriptions
        result.aliases mustBe q42.aliases
    }

    @Test
    fun `It encodes a Entity`() {
        // Given
        val expected = ResourceLoader.loader.load("/fixture/wikibase/Q42NoEncoding.json")

        // When
        val result = serializer.encodeToString(
            Entity.serializer(),
            q42
        )

        // Then
        result mustBe expected.trim()
    }
}
