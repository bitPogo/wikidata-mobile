/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase.model

import tech.antibytes.mediawiki.wikibase.WikibaseContract

internal data class SearchEntityResponse(
    val search: List<SearchEntity>,
    override val success: Int = 0
) : WikibaseContract.Response
