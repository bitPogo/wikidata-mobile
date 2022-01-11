/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.di

import io.ktor.client.features.cookies.AcceptAllCookiesStorage
import io.ktor.client.features.cookies.CookiesStorage
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module
import tech.antibytes.mediawiki.MwClientContract
import tech.antibytes.mediawiki.ServiceResponseWrapper
import tech.antibytes.mediawiki.coroutine.SuspendingFunctionWrapper
import tech.antibytes.mediawiki.serialization.JsonConfigurator
import tech.antibytes.mediawiki.serialization.JsonConfiguratorContract

internal fun resolveMwClientModule(): Module {
    return module {
        factory<Clock> { Clock.System }

        factory<JsonConfiguratorContract> {
            JsonConfigurator()
        }

        factory<Json> {
            Json { get<JsonConfiguratorContract>().configure(this) }
        }

        factory<CookiesStorage> { AcceptAllCookiesStorage() }

        factory<MwClientContract.SuspendingFunctionWrapperFactory> {
            SuspendingFunctionWrapper.Factory()
        }

        single<MwClientContract.ServiceResponseWrapper> {
            ServiceResponseWrapper(get(), get(), get())
        }
    }
}
