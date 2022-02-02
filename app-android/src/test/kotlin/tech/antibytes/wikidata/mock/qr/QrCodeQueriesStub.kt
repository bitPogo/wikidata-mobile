/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.mock.qr

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.TransactionWithReturn
import com.squareup.sqldelight.TransactionWithoutReturn
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError
import tech.antibytes.wikibase.store.database.QrCodeQueries

class QrCodeQueriesStub(
    var fetchQrCode: ((String) -> Query<String>)? = null,
    var addQrCode: ((String, String) -> Unit)? = null
) : QrCodeQueries, MockContract.Mock {
    override fun fetchQrCode(whereId: String): Query<String> {
        return fetchQrCode?.invoke(whereId)
            ?: throw MockError.MissingStub("Missing Sideeffect fetchQrCode")
    }

    override fun addQrCode(id: String, encodedQrCode: String) {
        return addQrCode?.invoke(id, encodedQrCode)
            ?: throw MockError.MissingStub("Missing Sideeffect addQrCode")
    }

    override fun transaction(noEnclosing: Boolean, body: TransactionWithoutReturn.() -> Unit) {
        TODO("Not yet implemented")
    }

    override fun <R> transactionWithResult(noEnclosing: Boolean, bodyWithReturn: TransactionWithReturn<R>.() -> R): R {
        TODO("Not yet implemented")
    }

    override fun clear() {
        fetchQrCode = null
        addQrCode = null
    }
}
