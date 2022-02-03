/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.lib.qr.data

import android.util.Base64

internal class Base64 : QrCodeDataContract.Base64 {
    override fun encodeToString(bytes: ByteArray): String {
        return Base64.encodeToString(
            bytes,
            Base64.NO_WRAP
        )
    }

    override fun decode(string: String): ByteArray {
        return Base64.decode(
            string,
            Base64.NO_WRAP
        )
    }
}
