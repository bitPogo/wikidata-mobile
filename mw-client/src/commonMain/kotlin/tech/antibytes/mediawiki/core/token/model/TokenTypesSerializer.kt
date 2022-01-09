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
import tech.antibytes.mediawiki.core.token.TokenServiceContract
import tech.antibytes.mediawiki.error.MwClientError

internal class TokenTypesSerializer: KSerializer<TokenServiceContract.TokenTypes> {
    private val mapping = TokenServiceContract.TokenTypes.values().associateBy { it.value }

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "TokenTypes",
        PrimitiveKind.STRING
    )

    override fun serialize(encoder: Encoder, value: TokenServiceContract.TokenTypes) {
        encoder.encodeString(value.value)
    }

    override fun deserialize(decoder: Decoder): TokenServiceContract.TokenTypes {
        val key = decoder.decodeString()
        return mapping.getOrElse(key) {
            throw MwClientError.InternalFailure("Unknown TokenType ($key).")
        }
    }
}
