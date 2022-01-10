/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tech.antibytes.mediawiki.EntityId

@Serializable(with = EntityTypesSerializer::class)
enum class EntityTypes {
    ITEM,
    LEXEME,
    PROPERTY
}

@Serializable
data class Entity(
    val id: EntityId,
    @SerialName("lastrevid")
    val revisionId: Long? = null,
    @SerialName("modified")
    val lastModification: Instant? = null,
    val type: EntityTypes,
    val labels: Map<String, Label>,
    val descriptions: Map<String, Description>,
    val aliases: Map<String, List<Alias>>
)

@Serializable
data class Label(
    val language: String,
    val value: String
)

@Serializable
data class Description(
    val language: String,
    val value: String
)

@Serializable
data class Alias(
    val language: String,
    val value: String
)
