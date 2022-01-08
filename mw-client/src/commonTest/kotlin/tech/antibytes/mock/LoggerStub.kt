/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mock

import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

class LoggerStub(
    var info: ((String) -> Unit)? = null,
    var error: ((Throwable, String?) -> Unit)? = null,
    var warn: ((String) -> Unit)? = null,
    var log: ((String) -> Unit)? = null
) : PublicApi.Logger, MockContract.Mock {

    override fun info(message: String) {
        info?.invoke(message) ?: throw MockError.MissingStub("Missing sideeffect info")
    }

    override fun warn(message: String) {
        warn?.invoke(message) ?: throw MockError.MissingStub("Missing sideeffect warn")
    }

    override fun error(exception: Throwable, message: String?) {
        error?.invoke(exception, message) ?: throw MockError.MissingStub("Missing sideeffect error")
    }

    override fun log(message: String) {
        log?.invoke(message) ?: throw MockError.MissingStub("Missing sideeffect log")
    }

    override fun clear() {
        info = null
        warn = null
        error = null
        log = null
    }
}
