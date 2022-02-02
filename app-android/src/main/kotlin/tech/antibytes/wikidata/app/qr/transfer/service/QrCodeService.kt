/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.qr.transfer.service

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import tech.antibytes.wikidata.app.qr.QrCodeStoreContract.Companion.SIZE
import tech.antibytes.wikidata.app.qr.transfer.QrCodeServiceContract

internal class QrCodeService(
    private val qrCodeWriter: QRCodeWriter = QRCodeWriter()
) : QrCodeServiceContract {

    private fun toByte(boolean: Boolean): Byte {
        return if (boolean) {
            1
        } else {
            0
        }
    }

    private fun convertIntoByteArray(qrCode: BitMatrix) : ByteArray {
        val array = ByteArray(ARRAY_SIZE)

        var idx = 0
        for (x in 0 until SIZE) {
            for (y in 0 until SIZE) {
                array[idx] = toByte(qrCode.get(x, y))
                idx++
            }
        }

        return array
    }

    override suspend fun create(url: String): ByteArray {
        val qrString = "url:$url"

        val matrix = qrCodeWriter.encode(
            qrString,
            BarcodeFormat.QR_CODE,
            SIZE,
            SIZE,
            HINTS
        )

        return convertIntoByteArray(matrix)
    }

    private companion object {
        val HINTS = mapOf(EncodeHintType.MARGIN to 1)
        const val ARRAY_SIZE = SIZE * SIZE
    }
}
