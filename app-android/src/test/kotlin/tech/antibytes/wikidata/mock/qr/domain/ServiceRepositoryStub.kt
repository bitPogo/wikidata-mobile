/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.mock.qr.domain

import android.graphics.Bitmap
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError
import tech.antibytes.wikidata.lib.qr.domain.DomainContract

class ServiceRepositoryStub(
    var createQrCode: ((String) -> Bitmap)? = null
) : DomainContract.ServiceRepository, MockContract.Mock {
    override suspend fun createQrCode(url: String): Bitmap {
        return createQrCode?.invoke(url)
            ?: throw MockError.MissingStub("Missing Sideeffect createQrCode")
    }

    override fun clear() {
        createQrCode = null
    }
}
