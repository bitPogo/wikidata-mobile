/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.json.Json
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.Test

class MatchTypeSerializerSpec {
    @Test
    fun `It fulfils KSerializer`() {
        MatchTypeSerializer() fulfils KSerializer::class
    }

    @Test
    fun `It has a Descriptor`() {
        val descriptor = MatchTypeSerializer().descriptor

        descriptor.serialName mustBe "MatchType"
        descriptor.kind mustBe PrimitiveKind.STRING
    }

    @Test
    fun `Given a Serializer is called with a MatchType, it encodes it`() {
        // Given
        val serializer = Json

        for (field in MatchTypes.values()) {
            // When
            val result = serializer.encodeToString(
                MatchTypeSerializer(),
                field
            )

            // Then
            result mustBe "\"${field.name.lowercase()}\""
        }
    }

    @Test
    fun `Given a Serializer is called with a serialized MatchType, it decodess it`() {
        // Given
        val serializer = Json

        val types = listOf(
            MatchTypes.LABEL.name.lowercase() to MatchTypes.LABEL,
            MatchTypes.DESCRIPTION.name.lowercase() to MatchTypes.DESCRIPTION,
            MatchTypes.ALIAS.name.lowercase() to MatchTypes.ALIAS
        )

        for (field in types) {
            // When
            val result = serializer.decodeFromString(
                MatchTypeSerializer(),
                "\"${field.first}\""
            )

            // Then
            result mustBe field.second
        }
    }
}
