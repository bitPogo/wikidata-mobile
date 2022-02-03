/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.lib.qr.domain

import android.graphics.Bitmap

interface DomainContract {
    interface ServiceRepository {
        suspend fun createQrCode(url: String): Bitmap
    }

    interface StorageRepository {
        suspend fun fetchQrCode(url: String): Bitmap?
        suspend fun storeQrCode(url: String, qrCode: Bitmap)
    }

    enum class DomainKoinIds {
        PRODUCER_SCOPE,
        CONSUMER_SCOPE
    }
}
