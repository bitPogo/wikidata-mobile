/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.qr.data

import android.graphics.Bitmap
import tech.antibytes.wikidata.app.qr.domain.DomainContract

internal class ServiceRepository(
    private val qrCodeService: QrCodeDataContract.Service,
    private val mapper: QrCodeDataContract.Mapper
) : DomainContract.ServiceRepository {
    override suspend fun createQrCode(url: String): Bitmap {
        return mapper.mapToBitmap(
            qrCodeService.create(url)
        )
    }
}
