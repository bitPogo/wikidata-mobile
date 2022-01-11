/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.token

import org.koin.core.module.Module
import org.koin.dsl.module
import tech.antibytes.mediawiki.annotation.InternalKoinModuleScope

internal fun resolveMetaTokenModule(): Module {
    return module {
        @InternalKoinModuleScope
        factory<MetaTokenContract.ApiService> {
            MetaTokenApiService(get())
        }

        single<MetaTokenContract.Repository> {
            MetaTokenRepository(get())
        }
    }
}
