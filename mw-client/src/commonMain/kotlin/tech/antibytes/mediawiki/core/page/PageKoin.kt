/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.page

import org.koin.core.module.Module
import org.koin.dsl.module
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.mediawiki.annotation.InternalKoinModuleScope

internal fun resolvePageModule(): Module {
    return module {
        @InternalKoinModuleScope
        factory<PageContract.ApiService> {
            PageApiService(get())
        }

        @InternalKoinModuleScope
        factory<PageContract.Repository> {
            PageRepository(get())
        }

        single<PublicApi.PageService> {
            PageService(get(), get())
        }
    }
}
