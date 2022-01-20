/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.page.transfer.mapper

import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.wikibase.store.page.domain.model.LanguageTag
import tech.antibytes.wikibase.store.page.domain.model.PageModelContract
import tech.antibytes.wikibase.store.page.domain.model.SearchEntry
import tech.antibytes.wikibase.store.page.transfer.DataTransferContract

internal class SearchEntityMapper : DataTransferContract.SearchEntityMapper {
    private fun extractLanguage(entity: DataModelContract.Entity): LanguageTag? {
        return entity.labels.keys.firstOrNull()
            ?: entity.descriptions.keys.firstOrNull()
    }

    private fun mapSearchEntry(
        language: LanguageTag,
        entity: DataModelContract.Entity
    ): PageModelContract.SearchEntry {
        return SearchEntry(
            id = entity.id,
            language = language,
            label = entity.labels[language]?.value,
            description = entity.descriptions[language]?.value,
        )
    }

    override fun map(entity: DataModelContract.Entity): PageModelContract.SearchEntry? {
        val language = extractLanguage(entity)

        return if (language == null) {
            null
        } else {
            mapSearchEntry(language, entity)
        }
    }
}
