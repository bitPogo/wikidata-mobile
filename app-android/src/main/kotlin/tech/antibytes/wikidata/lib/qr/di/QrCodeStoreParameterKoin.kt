/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.lib.qr.di

import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.CoroutineScopeDispatcher
import tech.antibytes.wikibase.store.database.QrCodeQueries
import tech.antibytes.wikidata.lib.qr.domain.DomainContract

internal fun resolveQrCodeStoreParameterModule(
    database: QrCodeQueries,
    producerScope: CoroutineScopeDispatcher,
    consumerScope: CoroutineScopeDispatcher
): Module {
    return module {
        factory { database }
        factory(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) { producerScope }
        factory(named(DomainContract.DomainKoinIds.CONSUMER_SCOPE)) { consumerScope }
    }
}
