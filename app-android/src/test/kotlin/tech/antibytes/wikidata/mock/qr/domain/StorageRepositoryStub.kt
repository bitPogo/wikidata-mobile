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

class StorageRepositoryStub(
    var fetchQrCode: ((String) -> Bitmap?)? = null,
    var storeQrCode: ((String, Bitmap) -> Unit)? = null
) : DomainContract.StorageRepository, MockContract.Mock {
    override suspend fun fetchQrCode(url: String): Bitmap? {
        return if (fetchQrCode != null) {
            fetchQrCode!!.invoke(url)
        } else {
            throw MockError.MissingStub("Missing Sideeffect fetchQrCode")
        }
    }

    override suspend fun storeQrCode(url: String, qrCode: Bitmap) {
        return storeQrCode?.invoke(url, qrCode)
            ?: throw MockError.MissingStub("Missing Sideeffect storeQrCode")
    }

    override fun clear() {
        fetchQrCode = null
        storeQrCode = null
    }
}
