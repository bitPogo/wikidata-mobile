/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase.model

import kotlinx.serialization.Serializable
import tech.antibytes.mediawiki.EntityId

internal enum class MatchTypes {
    LABEL,
    DESCRIPTION,
    ALIAS
}

@Serializable
internal data class SearchEntity(
    val id: EntityId,
    val label: String = "",
    val description: String = "",
    val aliases: List<String> = emptyList(),
    val match: Match
)

@Serializable
internal data class Match(
    val type: MatchTypes
)
