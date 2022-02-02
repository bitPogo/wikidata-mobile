/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.qr.data

import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.get
import androidx.core.graphics.set
import tech.antibytes.wikidata.app.qr.QrCodeStoreContract.Companion.ARRAY_SIZE
import tech.antibytes.wikidata.app.qr.QrCodeStoreContract.Companion.SIZE

internal class BitmapMapper(
    private val base64: QrCodeDataContract.Base64,
    private val size: Int = SIZE,
    private val arraySize: Int = ARRAY_SIZE
) : QrCodeDataContract.Mapper {
    private fun mapToColor(byte: Byte): Int {
        return if (byte.toInt() == 1) {
            Color.BLACK
        } else {
            Color.WHITE
        }
    }

    private fun mapToByte(color: Int): Byte {
        return if (color == Color.BLACK) {
            1
        } else {
            0
        }
    }

    private fun applyOnBitmap(action: (Int, Int, Int) -> Unit) {
        var idx = 0
        for (x in 0 until size) {
            for (y in 0 until size) {
                action.invoke(x, y, idx)
                idx++
            }
        }
    }

    override fun mapToBitmap(bytes: ByteArray): Bitmap {
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)

        applyOnBitmap { x, y, idx ->
            bitmap[x, y] = mapToColor(bytes[idx])
        }

        return bitmap
    }

    override fun mapToBitmap(encoded: String): Bitmap = mapToBitmap(base64.decode(encoded))

    private fun mapToByteArray(bitmap: Bitmap): ByteArray {
        val mapped = ByteArray(arraySize)

        applyOnBitmap { x, y, idx ->
            mapped[idx] = mapToByte(bitmap[x, y])
        }

        return mapped
    }

    override fun mapToString(bitmap: Bitmap): String = base64.encodeToString(mapToByteArray(bitmap))
}
