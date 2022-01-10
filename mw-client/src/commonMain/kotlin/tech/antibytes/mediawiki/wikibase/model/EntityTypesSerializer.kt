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

internal class EntityTypesSerializer : KSerializer<DataModelContract.EntityTypes> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "EntityTypes",
        PrimitiveKind.STRING
    )

    override fun serialize(encoder: Encoder, value: DataModelContract.EntityTypes) {
        encoder.encodeString(value.name.lowercase())
    }

    override fun deserialize(decoder: Decoder): DataModelContract.EntityTypes {
        val key = decoder.decodeString()
        return DataModelContract.EntityTypes.valueOf(key.uppercase())
    }
}
