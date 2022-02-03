/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.lib.qr.data

import org.koin.core.module.Module
import org.koin.dsl.module
import tech.antibytes.mediawiki.annotation.InternalKoinModuleScope
import tech.antibytes.wikidata.lib.qr.data.repository.QrCodeServiceRepository
import tech.antibytes.wikidata.lib.qr.data.repository.QrCodeStorageRepository
import tech.antibytes.wikidata.lib.qr.domain.DomainContract

fun resolveQrCodeDataServiceModule(): Module {
    return module {
        @InternalKoinModuleScope
        factory<QrCodeDataContract.Base64> { Base64() }

        @InternalKoinModuleScope
        factory<QrCodeDataContract.Mapper> { BitmapMapper(get()) }

        @InternalKoinModuleScope
        factory<QrCodeDataContract.Service> { QrCodeService() }

        factory<DomainContract.ServiceRepository> {
            QrCodeServiceRepository(get(), get())
        }

        factory<DomainContract.StorageRepository> {
            QrCodeStorageRepository(get(), get())
        }
    }
}
