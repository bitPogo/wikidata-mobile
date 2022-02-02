/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.mock.qr.transfer

import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError
import tech.antibytes.wikidata.app.qr.data.QrCodeDataContract

internal class Base64Stub(
    var encodeToString: ((ByteArray) -> String)? = null,
    var decode: ((String) -> ByteArray)? = null
) : QrCodeDataContract.Base64, MockContract.Mock {
    override fun encodeToString(bytes: ByteArray): String {
        return encodeToString?.invoke(bytes)
            ?: throw MockError.MissingStub("Missing Sideeffect encodeToString")
    }

    override fun decode(string: String): ByteArray {
        return decode?.invoke(string)
            ?: throw MockError.MissingStub("Missing Sideeffect decode")
    }

    override fun clear() {
        encodeToString = null
        decode = null
    }
}
