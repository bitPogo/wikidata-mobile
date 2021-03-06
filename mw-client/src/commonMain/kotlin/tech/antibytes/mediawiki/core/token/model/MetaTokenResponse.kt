/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.token.model

import kotlinx.serialization.Serializable
import tech.antibytes.mediawiki.core.token.MetaTokenContract

@Serializable
internal data class MetaTokenResponse(
    val query: Query
)

@Serializable
internal data class Query(
    val tokens: Map<MetaTokenContract.MetaTokenType, String>
)
