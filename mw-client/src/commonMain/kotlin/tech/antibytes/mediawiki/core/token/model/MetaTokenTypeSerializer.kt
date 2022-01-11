/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.token.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import tech.antibytes.mediawiki.core.token.MetaTokenContract
import tech.antibytes.mediawiki.error.MwClientError

internal class MetaTokenTypeSerializer : KSerializer<MetaTokenContract.MetaTokenType> {
    private val mapping = MetaTokenContract.MetaTokenType.values().associateBy { it.value }

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "MetaTokenType",
        PrimitiveKind.STRING
    )

    override fun serialize(encoder: Encoder, value: MetaTokenContract.MetaTokenType) {
        encoder.encodeString(value.value)
    }

    override fun deserialize(decoder: Decoder): MetaTokenContract.MetaTokenType {
        val key = decoder.decodeString()
        return mapping.getOrElse(key) {
            throw MwClientError.InternalFailure("Unknown TokenType ($key).")
        }
    }
}
