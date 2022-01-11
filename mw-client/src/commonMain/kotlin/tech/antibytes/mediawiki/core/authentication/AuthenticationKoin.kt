/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.authentication

import org.koin.core.module.Module
import org.koin.dsl.module
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.mediawiki.annotation.InternalKoinModuleScope

internal fun resolveAuthenticationModule(): Module {
    return module {
        @InternalKoinModuleScope
        factory<AuthenticationContract.ApiService> {
            AuthenticationApiService(get())
        }

        @InternalKoinModuleScope
        factory<AuthenticationContract.Repository> {
            AuthenticationRepository(get())
        }

        single<PublicApi.AuthenticationService> {
            AuthenticationService(get(), get(), get())
        }
    }
}
