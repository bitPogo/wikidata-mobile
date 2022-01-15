/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import tech.antibytes.mediawiki.wikibase.model.EntityTypeSerializer

interface DataModelContract {
    @Serializable(with = EntityTypeSerializer::class)
    enum class EntityType {
        ITEM,
        LEXEME,
        PROPERTY
    }

    interface LanguageValuePair {
        val language: LanguageTag
        val value: String
    }

    interface BoxedTerms {
        val labels: Map<String, LanguageValuePair>
        val descriptions: Map<String, LanguageValuePair>
        val aliases: Map<String, List<LanguageValuePair>>
    }

    interface Entity : BoxedTerms {
        val id: EntityId
        val type: EntityType
    }

    interface RevisionedEntity : Entity {
        val revision: Long
        val lastModification: Instant
    }

    interface RevisionedPagePointer {
        val revisionId: Long
        val title: String
    }
}
