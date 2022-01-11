/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mock

import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

internal class ConnectivityManagerStub(
    var hasConnection: (() -> Boolean)? = null
) : PublicApi.ConnectivityManager, MockContract.Mock {
    override fun hasConnection(): Boolean {
        return hasConnection?.invoke() ?: throw MockError.MissingStub("Missing Sideeffect hasConnection")
    }

    override fun clear() {
        hasConnection = null
    }
}
