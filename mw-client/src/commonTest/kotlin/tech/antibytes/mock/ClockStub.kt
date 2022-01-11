/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mock

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

internal class ClockStub(
    var now: (() -> Instant)? = null
) : Clock, MockContract.Mock {
    override fun now(): Instant {
        return now?.invoke() ?: throw MockError.MissingStub("Missing Sideeffect now")
    }

    override fun clear() {
        now = null
    }
}
