/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.json.Json
import tech.antibytes.mediawiki.EntityContract
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.Test

class EntityTypesSerializerSpec {
    @Test
    fun `It fulfils KSerializer`() {
        EntityTypesSerializer() fulfils KSerializer::class
    }

    @Test
    fun `It has a Descriptor`() {
        val descriptor = EntityTypesSerializer().descriptor

        descriptor.serialName mustBe "EntityTypes"
        descriptor.kind mustBe PrimitiveKind.STRING
    }

    @Test
    fun `Given a Serializer is called with a EntityType, it encodes it`() {
        // Given
        val serializer = Json

        for (field in EntityContract.EntityTypes.values()) {
            // When
            val result = serializer.encodeToString(
                EntityTypesSerializer(),
                field
            )

            // Then
            result mustBe "\"${field.name.lowercase()}\""
        }
    }

    @Test
    fun `Given a Serializer is called with a serialized EntityType, it decodess it`() {
        // Given
        val serializer = Json

        val types = listOf(
            EntityContract.EntityTypes.ITEM.name.lowercase() to EntityContract.EntityTypes.ITEM,
            EntityContract.EntityTypes.LEXEME.name.lowercase() to EntityContract.EntityTypes.LEXEME,
            EntityContract.EntityTypes.PROPERTY.name.lowercase() to EntityContract.EntityTypes.PROPERTY
        )

        for (field in types) {
            // When
            val result = serializer.decodeFromString(
                EntityTypesSerializer(),
                "\"${field.first}\""
            )

            // Then
            result mustBe field.second
        }
    }
}
