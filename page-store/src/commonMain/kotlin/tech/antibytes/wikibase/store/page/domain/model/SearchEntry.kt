/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.page.domain.model

internal data class SearchEntry(
    override val id: EntityId,
    override val language: LanguageTag,
    override val label: String?,
    override val description: String?
) : PageModelContract.SearchEntry
