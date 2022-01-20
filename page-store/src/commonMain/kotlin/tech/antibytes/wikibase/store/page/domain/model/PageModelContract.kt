/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.page.domain.model

typealias EntityId = String
typealias LanguageTag = String

interface PageModelContract {
    interface SearchEntry {
        val id: EntityId
        val language: LanguageTag
        val label: String?
        val description: String?
    }
}
