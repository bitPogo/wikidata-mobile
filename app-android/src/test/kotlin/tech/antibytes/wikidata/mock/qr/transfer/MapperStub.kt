/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.mock.qr.transfer

import android.graphics.Bitmap
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError
import tech.antibytes.wikidata.lib.qr.data.QrCodeDataContract

internal class MapperStub(
    var mapToBitmap: ((ByteArray) -> Bitmap)? = null,
    var mapFromString: ((String) -> Bitmap)? = null,
    var mapToString: ((Bitmap) -> String)? = null
) : QrCodeDataContract.Mapper, MockContract.Mock {
    override fun mapToBitmap(bytes: ByteArray): Bitmap {
        return mapToBitmap?.invoke(bytes)
            ?: throw MockError.MissingStub("Missing Sideeffect mapToBitmap")
    }

    override fun mapToBitmap(encoded: String): Bitmap {
        return mapFromString?.invoke(encoded)
            ?: throw MockError.MissingStub("Missing Sideeffect mapFromString (mapFromString)")
    }

    override fun mapToString(bitmap: Bitmap): String {
        return mapToString?.invoke(bitmap)
            ?: throw MockError.MissingStub("Missing Sideeffect mapToString")
    }

    override fun clear() {
        mapToBitmap = null
        mapFromString = null
        mapToString = null
    }
}
