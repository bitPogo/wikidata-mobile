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
import tech.antibytes.mediawiki.core.token.MetaTokenServiceContract
import tech.antibytes.mediawiki.error.MwClientError

internal class MetaTokenTypesSerializer : KSerializer<MetaTokenServiceContract.TokenTypes> {
    private val mapping = MetaTokenServiceContract.TokenTypes.values().associateBy { it.value }

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "TokenTypes",
        PrimitiveKind.STRING
    )

    override fun serialize(encoder: Encoder, value: MetaTokenServiceContract.TokenTypes) {
        encoder.encodeString(value.value)
    }

    override fun deserialize(decoder: Decoder): MetaTokenServiceContract.TokenTypes {
        val key = decoder.decodeString()
        return mapping.getOrElse(key) {
            throw MwClientError.InternalFailure("Unknown TokenType ($key).")
        }
    }
}
