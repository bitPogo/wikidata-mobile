/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mock.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import tech.antibytes.mediawiki.DataModelContract.BoxedTerms
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

internal class BoxedTermsSerializerStub(
    var valueDescriptor: SerialDescriptor? = null,
    var serialize: ((encoder: Encoder, value: BoxedTerms) -> Unit)? = null,
    var deserialize: ((decoder: Decoder) -> BoxedTerms)? = null
): KSerializer<BoxedTerms>, MockContract.Mock {
    override val descriptor: SerialDescriptor
        get() {
            return valueDescriptor
                ?: throw MockError.MissingStub("Missing Sideeffect valueDescriptor")
        }

    override fun deserialize(decoder: Decoder): BoxedTerms {
        return deserialize?.invoke(decoder) ?: throw MockError.MissingStub("Missing Sideeffect deserialize")
    }

    override fun serialize(encoder: Encoder, value: BoxedTerms) {
        return serialize?.invoke(encoder, value) ?: throw MockError.MissingStub("Missing Sideeffect serialize")
    }

    override fun clear() {
        deserialize = null
        serialize = null
        valueDescriptor = null
    }
}
