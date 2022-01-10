/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase.model

import kotlinx.serialization.Serializable
import tech.antibytes.mediawiki.EntityId

@Serializable
internal data class EntityResponse(
    val entities: Map<EntityId, Entity> = emptyMap(),
    val success: Int = 0
)
