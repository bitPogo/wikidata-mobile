/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase.model

import kotlinx.serialization.Serializable
import tech.antibytes.mediawiki.EntityId
import tech.antibytes.mediawiki.wikibase.WikibaseContract

internal enum class MatchTypes {
    LABEL,
    DESCRIPTION,
    ALIAS
}

@Serializable
internal data class SearchEntity(
    override val id: EntityId,
    override val label: String = "",
    override val description: String = "",
    override val aliases: List<String> = emptyList(),
    val match: Match
) : WikibaseContract.MonolingualEntity

@Serializable
internal data class Match(
    val type: MatchTypes
)
