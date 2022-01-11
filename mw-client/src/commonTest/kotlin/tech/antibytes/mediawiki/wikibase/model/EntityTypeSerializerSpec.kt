/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.json.Json
import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.Test

class EntityTypeSerializerSpec {
    @Test
    fun `It fulfils KSerializer`() {
        EntityTypeSerializer() fulfils KSerializer::class
    }

    @Test
    fun `It has a Descriptor`() {
        val descriptor = EntityTypeSerializer().descriptor

        descriptor.serialName mustBe "EntityType"
        descriptor.kind mustBe PrimitiveKind.STRING
    }

    @Test
    fun `Given a Serializer is called with a EntityType, it encodes it`() {
        // Given
        val serializer = Json

        for (field in DataModelContract.EntityType.values()) {
            // When
            val result = serializer.encodeToString(
                EntityTypeSerializer(),
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
            DataModelContract.EntityType.ITEM.name.lowercase() to DataModelContract.EntityType.ITEM,
            DataModelContract.EntityType.LEXEME.name.lowercase() to DataModelContract.EntityType.LEXEME,
            DataModelContract.EntityType.PROPERTY.name.lowercase() to DataModelContract.EntityType.PROPERTY
        )

        for (field in types) {
            // When
            val result = serializer.decodeFromString(
                EntityTypeSerializer(),
                "\"${field.first}\""
            )

            // Then
            result mustBe field.second
        }
    }
}
