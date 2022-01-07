/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mock.serialization

import kotlinx.serialization.json.JsonBuilder
import tech.antibytes.mediawiki.networking.plugin.KtorPluginsContract
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

class JsonConfiguratorStub(
    var configure: ((JsonBuilder) -> JsonBuilder)? = null
) : KtorPluginsContract.JsonConfiguratorContract, MockContract.Mock {


    override fun configure(jsonBuilder: JsonBuilder): JsonBuilder {
        return configure?.invoke(jsonBuilder) ?: throw MockError.MissingStub("Missing sideeffect configure")
    }

    override fun clear() {
        configure = null
    }
}
