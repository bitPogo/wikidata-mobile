/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.mock.qr.data

import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError
import tech.antibytes.wikidata.lib.qr.data.QrCodeDataContract

internal class QrCodeServiceStub(
    var create: ((String) -> ByteArray)? = null
) : QrCodeDataContract.Service, MockContract.Mock {
    override suspend fun create(url: String): ByteArray {
        return create?.invoke(url)
            ?: throw MockError.MissingStub("Missing Sideeffect create")
    }

    override fun clear() {
        create = null
    }
}
