/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.mock.database

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.TransactionWithReturn
import com.squareup.sqldelight.TransactionWithoutReturn
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError
import tech.antibytes.wikibase.store.database.page.PageQueries

class PageQueriesStub(
    var peekRandomPages: (() -> Query<String>)? = null,
    var insertRandomPage: ((String) -> Unit)? = null,
    var deleteRandomPage: ((String) -> Unit)? = null
) : PageQueries, MockContract.Mock {
    var lastTransaction: TransactionWithoutReturn? = null

    override fun peekRandomPages(): Query<String> {
        return peekRandomPages?.invoke()
            ?: throw MockError.MissingStub("Missing Sideeffect peekRandomPages")
    }

    override fun insertRandomPage(pageId: String) {
        return insertRandomPage?.invoke(pageId)
            ?: throw MockError.MissingStub("Missing Sideeffect insertRandomPage")
    }

    override fun deleteRandomPage(whereId: String) {
        return deleteRandomPage?.invoke(whereId)
            ?: throw MockError.MissingStub("Missing Sideeffect deleteRandomPage")
    }

    override fun transaction(noEnclosing: Boolean, body: TransactionWithoutReturn.() -> Unit) {
        lastTransaction = TransactionWithoutReturnStub(body)

        body.invoke(lastTransaction!!)
    }

    override fun <R> transactionWithResult(noEnclosing: Boolean, bodyWithReturn: TransactionWithReturn<R>.() -> R): R {
        TODO("Not yet implemented")
    }

    override fun clear() {
        lastTransaction = null
        peekRandomPages = null
        insertRandomPage = null
        deleteRandomPage = null
    }
}

class TransactionWithoutReturnStub(
    val capturedCall: (TransactionWithoutReturn) -> Unit
) : TransactionWithoutReturn {
    override fun afterCommit(function: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun afterRollback(function: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun rollback(): Nothing {
        TODO("Not yet implemented")
    }

    override fun transaction(body: TransactionWithoutReturn.() -> Unit) {
        TODO("Not yet implemented")
    }
}
