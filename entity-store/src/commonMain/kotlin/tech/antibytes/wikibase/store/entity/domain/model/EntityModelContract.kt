/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.domain.model

typealias EntityId = String
typealias LanguageTag = String

interface EntityModelContract {
    enum class EntityType {
        ITEM,
        PROPERTY,
        LEXEME
    }

    interface MonolingualEntity {
        val id: EntityId
        val type: EntityType
        val revision: Long
        val isEditable: Boolean
        val language: LanguageTag
        val label: String?
        val description: String?
        val aliases: List<String>
    }
}
