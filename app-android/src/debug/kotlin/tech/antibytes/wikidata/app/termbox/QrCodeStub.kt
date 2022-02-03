/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termbox

import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.set
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter

object QrCodeStub {
    val qrCode = Bitmap.createBitmap(
        175,
        175,
        Bitmap.Config.RGB_565
    ).also {
        val qrMatrix = QRCodeWriter().encode(
            "https://wikidata.org/wiki/Q42",
            BarcodeFormat.QR_CODE,
            175,
            175,
            mapOf(EncodeHintType.MARGIN to 1)
        )
        for (x in 0 until 175) {
            for (y in 0 until 175) {
                it[x, y] = if (qrMatrix[x, y]) {
                    Color.BLACK
                } else {
                    Color.WHITE
                }
            }
        }
    }
}
