/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.lib.qr.domain

import android.graphics.Bitmap
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import tech.antibytes.util.coroutine.result.Failure
import tech.antibytes.util.coroutine.result.ResultContract
import tech.antibytes.util.coroutine.result.Success
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.CoroutineScopeDispatcher
import tech.antibytes.util.coroutine.wrapper.SharedFlowWrapper
import tech.antibytes.wikidata.app.di.FlowDispatcher
import tech.antibytes.wikidata.app.di.IODispatcher
import tech.antibytes.wikidata.lib.qr.QrCodeStoreContract

class QrCodeStore internal constructor(
    private val serviceRepository: DomainContract.ServiceRepository,
    private val storageRepository: DomainContract.StorageRepository,
    @IODispatcher private val ioDispatcher: CoroutineScopeDispatcher,
    @FlowDispatcher flowDispatcher: CoroutineScopeDispatcher
) : QrCodeStoreContract.QrCodeStore {
    private val flow: MutableSharedFlow<ResultContract<Bitmap, Exception>> = MutableSharedFlow()

    override val qrCode: CoroutineWrapperContract.SharedFlowWrapper<Bitmap, Exception> = SharedFlowWrapper.getInstance(
        flow,
        flowDispatcher
    )

    private suspend fun wrapResult(
        action: suspend () -> Bitmap
    ): ResultContract<Bitmap, Exception> {
        return try {
            Success(action.invoke())
        } catch (error: Exception) {
            Failure(error)
        }
    }

    private fun executeEvent(event: suspend () -> Bitmap) {
        ioDispatcher.dispatch().launch {
            flow.emit(wrapResult(event))
        }
    }

    private suspend fun createAndStore(url: String): Bitmap {
        val bitmap = serviceRepository.createQrCode(url)
        storageRepository.storeQrCode(url, bitmap)

        return bitmap
    }

    private suspend fun fetchOrCreate(url: String): Bitmap {
        return storageRepository.fetchQrCode(url) ?: createAndStore(url)
    }

    override fun fetch(url: String) {
        executeEvent {
            fetchOrCreate(url)
        }
    }
}
