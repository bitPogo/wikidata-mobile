/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import tech.antibytes.mediawiki.DataModelContract

internal class LanguageValuePairSerializer : KSerializer<DataModelContract.LanguageValuePair> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("LanguageValuePair") {
        element<String>("language")
        element<String>("value")
    }

    override fun serialize(encoder: Encoder, value: DataModelContract.LanguageValuePair) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.language)
            encodeStringElement(descriptor, 1, value.value)
        }
    }

    override fun deserialize(decoder: Decoder): DataModelContract.LanguageValuePair {
        return decoder.decodeStructure(descriptor) {
            var language: String? = null
            var value: String? = null

            while (true) {
                when (decodeElementIndex(descriptor)) {
                    0 -> language = decodeStringElement(descriptor, 0)
                    1 -> value = decodeStringElement(descriptor, 1)
                    CompositeDecoder.DECODE_DONE -> break
                }
            }
            require(language != null && value != null)
            LanguageValuePair(language, value)
        }
    }
}
