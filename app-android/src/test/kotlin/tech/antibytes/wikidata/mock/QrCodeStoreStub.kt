/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.mock

import android.graphics.Bitmap
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError
import tech.antibytes.wikidata.lib.qr.QrCodeStoreContract

class QrCodeStoreStub(
    override val qrCode: CoroutineWrapperContract.SharedFlowWrapper<Bitmap, Exception>,
    var fetch: ((String) -> Unit)? = null
) : QrCodeStoreContract.QrCodeStore, MockContract.Mock {
    override fun fetch(url: String) {
        return fetch?.invoke(url)
            ?: throw MockError.MissingStub("Missing Sideeffect fetch")
    }

    override fun clear() {
        fetch = null
    }
}
