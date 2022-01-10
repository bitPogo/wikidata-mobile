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
    override val revisionId: Long
) : DataModelContract.RevisionedPagePointer

@Serializable
internal data class Restrictions(
    @SerialName("restrictiontypes")
    val restrictions: List<String>
)

@Serializable
internal data class Query(
    val random: Map<String, Page> = emptyMap(),
    val pages: Map<String, Restrictions> = emptyMap()
)

@Serializable
internal data class PageResponse(
    val query: Query
)
