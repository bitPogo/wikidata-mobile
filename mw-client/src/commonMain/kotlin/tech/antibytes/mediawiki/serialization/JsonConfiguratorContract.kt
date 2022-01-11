/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.serialization

import kotlinx.serialization.json.JsonBuilder

interface JsonConfiguratorContract {
    fun configure(jsonBuilder: JsonBuilder): JsonBuilder
}
