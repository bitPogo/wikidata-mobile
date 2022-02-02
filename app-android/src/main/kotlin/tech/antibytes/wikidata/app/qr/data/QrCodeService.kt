/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.qr.data

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import tech.antibytes.wikidata.app.qr.QrCodeStoreContract.Companion.ARRAY_SIZE
import tech.antibytes.wikidata.app.qr.QrCodeStoreContract.Companion.SIZE

internal class QrCodeService(
    private val qrCodeWriter: QRCodeWriter = QRCodeWriter(),
    private val size: Int = SIZE,
    private val arraySize: Int = ARRAY_SIZE
) : QrCodeDataContract.Service {

    private fun toByte(boolean: Boolean): Byte {
        return if (boolean) {
            1
        } else {
            0
        }
    }

    private fun convertIntoByteArray(qrCode: BitMatrix): ByteArray {
        val array = ByteArray(arraySize)

        var idx = 0
        for (x in 0 until size) {
            for (y in 0 until size) {
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
    }
}
