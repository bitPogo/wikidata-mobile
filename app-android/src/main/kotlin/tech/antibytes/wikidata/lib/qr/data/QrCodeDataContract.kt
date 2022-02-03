/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.lib.qr.data

import android.graphics.Bitmap

internal interface QrCodeDataContract {
    interface Service {
        suspend fun create(url: String): ByteArray
    }

    interface Storage {
        suspend fun store(bitmap: Bitmap)
        suspend fun fetch(url: String): Bitmap
    }

    interface Mapper {
        fun mapToBitmap(bytes: ByteArray): Bitmap
        fun mapToBitmap(encoded: String): Bitmap
        fun mapToString(bitmap: Bitmap): String
    }

    interface Base64 {
        fun encodeToString(bytes: ByteArray): String
        fun decode(string: String): ByteArray
    }
}
