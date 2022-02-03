/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.lib.qr

import android.graphics.Bitmap
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.CoroutineScopeDispatcher
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.SharedFlowWrapper
import tech.antibytes.wikibase.store.database.QrCodeQueries

interface QrCodeStoreContract {
    interface QrCodeStore {
        val qrCode: SharedFlowWrapper<Bitmap, Exception>

        fun fetch(url: String)
    }

    interface QrCodeStoreFactory {
        fun getInstance(
            database: QrCodeQueries,
            producerScope: CoroutineScopeDispatcher,
            consumerScope: CoroutineScopeDispatcher
        ): QrCodeStore
    }

    companion object {
        const val SIZE = 175
        const val ARRAY_SIZE = SIZE * SIZE
    }
}
