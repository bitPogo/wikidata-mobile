/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.page.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tech.antibytes.mediawiki.DataModelContract

@Serializable
internal data class Page(
    override val title: String,
    @SerialName("lastrevid")
    override val revisionId: Long,
    @SerialName("restrictiontypes")
    val restrictions: List<String> = emptyList(),
    // TODO This should be smooth with a MwApiExpert
    @SerialName("protection")
    val protectionLevels: List<Map<String, String>> = emptyList()
) : DataModelContract.RevisionedPagePointer

@Serializable
internal data class Query(
    val pages: Map<String, Page> = emptyMap()
)

@Serializable
internal data class PageResponse(
    val query: Query
)
