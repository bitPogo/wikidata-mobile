/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mock.networking.plugin

import tech.antibytes.mediawiki.networking.plugin.KtorPluginsContract
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

internal class ErrorMapperStub(
    var propagate: ((error: Throwable) -> Unit)? = null
) : KtorPluginsContract.ErrorMapper, MockContract.Mock {

    override fun mapAndThrow(error: Throwable) {
        propagate?.invoke(error) ?: throw MockError.MissingStub("Missing sideeffect of mapAndThrow")
    }

    override fun clear() {
        propagate = null
    }
}
