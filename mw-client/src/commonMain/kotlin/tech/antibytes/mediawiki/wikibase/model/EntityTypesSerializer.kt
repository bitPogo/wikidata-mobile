/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import tech.antibytes.mediawiki.EntityContract

internal class EntityTypesSerializer : KSerializer<EntityContract.EntityTypes> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "EntityTypes",
        PrimitiveKind.STRING
    )

    override fun serialize(encoder: Encoder, value: EntityContract.EntityTypes) {
        encoder.encodeString(value.name.lowercase())
    }

    override fun deserialize(decoder: Decoder): EntityContract.EntityTypes {
        val key = decoder.decodeString()
        return EntityContract.EntityTypes.valueOf(key.uppercase())
    }
}
