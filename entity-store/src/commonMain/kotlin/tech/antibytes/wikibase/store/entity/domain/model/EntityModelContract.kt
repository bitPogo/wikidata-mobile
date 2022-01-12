/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.domain.model

typealias EntityId = String
typealias LanguageTag = String

interface EntityModelContract {
    interface MonolingualEntity {
        val id: EntityId
        val isEditable: Boolean
        val label: String?
        val description: String?
        val aliases: List<String>
    }
}
