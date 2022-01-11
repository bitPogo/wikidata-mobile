/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.mediawiki.EntityId

@Serializable
internal data class Entity(
    override val id: EntityId,
    @SerialName("lastrevid")
    override val revisionId: Long = -1,
    @SerialName("modified")
    override val lastModification: Instant = Instant.DISTANT_PAST,
    override val type: DataModelContract.EntityType,
    override val labels: Map<String, LanguageValuePair>,
    override val descriptions: Map<String, LanguageValuePair>,
    override val aliases: Map<String, List<LanguageValuePair>>
) : DataModelContract.RevisionedEntity

@Serializable
internal data class LanguageValuePair(
    override val language: String,
    override val value: String
) : DataModelContract.LanguageValuePair
