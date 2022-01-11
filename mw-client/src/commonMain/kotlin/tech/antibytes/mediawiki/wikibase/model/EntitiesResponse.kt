/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase.model

import kotlinx.serialization.Serializable
import tech.antibytes.mediawiki.EntityId
import tech.antibytes.mediawiki.wikibase.WikibaseContract

@Serializable
internal data class EntitiesResponse(
    val entities: Map<EntityId, Entity> = emptyMap(),
    override val success: Int = 0
) : WikibaseContract.Response
