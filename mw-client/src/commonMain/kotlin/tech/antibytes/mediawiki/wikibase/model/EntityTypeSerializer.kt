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
import tech.antibytes.mediawiki.DataModelContract

internal class EntityTypeSerializer : KSerializer<DataModelContract.EntityType> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "EntityType",
        PrimitiveKind.STRING
    )

    override fun serialize(encoder: Encoder, value: DataModelContract.EntityType) {
        encoder.encodeString(value.name.lowercase())
    }

    override fun deserialize(decoder: Decoder): DataModelContract.EntityType {
        val key = decoder.decodeString()
        return DataModelContract.EntityType.valueOf(key.uppercase())
    }
}
