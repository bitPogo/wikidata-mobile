/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure
import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.mediawiki.DataModelContract.BoxedTerms
import tech.antibytes.mediawiki.DataModelContract.LanguageValuePair
import tech.antibytes.mediawiki.EntityId

internal class BoxedTermsSerializer : KSerializer<BoxedTerms> {
    private val languageValuePairMapSerializer = MapSerializer(String.serializer(), LanguageValuePairSerializer())
    private val aliasesSerializer = MapSerializer(
        String.serializer(),
        ListSerializer(LanguageValuePairSerializer())
    )

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("BoxedTerms") {
        element<Map<String, LanguageValuePair>>("labels")
        element<Map<String, LanguageValuePair>>("descriptions")
        element<Map<String, List<LanguageValuePair>>>("aliases")
    }

    override fun serialize(encoder: Encoder, value: BoxedTerms) {
        encoder.encodeStructure(descriptor) {
            encodeSerializableElement(
                descriptor,
                0,
                languageValuePairMapSerializer,
                value.labels
            )
            encodeSerializableElement(
                descriptor,
                1,
                languageValuePairMapSerializer,
                value.descriptions
            )

            encodeSerializableElement(
                descriptor,
                2,
                aliasesSerializer,
                value.aliases
            )
        }
    }

    override fun deserialize(decoder: Decoder): BoxedTerms {
        TODO("Not yet implemented")
    }
}
