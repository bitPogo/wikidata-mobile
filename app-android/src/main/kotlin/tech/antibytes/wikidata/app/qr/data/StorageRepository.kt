/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.qr.data

import android.graphics.Bitmap
import tech.antibytes.wikibase.store.database.QrCodeQueries
import tech.antibytes.wikidata.app.qr.domain.DomainContract

internal class StorageRepository(
    private val storage: QrCodeQueries,
    private val mapper: QrCodeDataContract.Mapper
) : DomainContract.StorageRepository {
    override suspend fun fetchQrCode(url: String): Bitmap? {
        val encoded = storage.fetchQrCode(url).executeAsOneOrNull()

        return if (encoded == null) {
            null
        } else {
            mapper.mapToBitmap(encoded)
        }
    }

    override suspend fun storeQrCode(url: String, qrCode: Bitmap) {
        val encoded = mapper.mapToString(qrCode)

        return storage.addQrCode(url, encoded)
    }
}
