/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.page

import org.koin.dsl.koinApplication
import org.koin.dsl.module
import tech.antibytes.mediawiki.MwClientContract
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.mediawiki.networking.NetworkingContract
import tech.antibytes.mock.ServiceResponseWrapperStub
import tech.antibytes.mock.core.page.PageApiServiceStub
import tech.antibytes.mock.core.page.PageRepositoryStub
import tech.antibytes.mock.networking.RequestBuilderFactoryStub
import tech.antibytes.util.test.isNot
import kotlin.test.Test

class PageKoinSpec {
    @Test
    fun `Given resolvePageModule is called it creates a Module, which contains a plain PageApiService`() {
        // When
        val koin = koinApplication {
            modules(
                resolvePageModule(),
                module {
                    single<NetworkingContract.RequestBuilderFactory> { RequestBuilderFactoryStub() }
                }
            )
        }

        val apiService: PageContract.ApiService = koin.koin.get()

        // Then
        apiService isNot null
    }

    @Test
    fun `Given resolvePageModule is called it creates a Module, which contains a plain PageRepository`() {
        // When
        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolvePageModule(),
                module {
                    single<PageContract.ApiService> { PageApiServiceStub() }
                }
            )
        }

        val repository: PageContract.Repository = koin.koin.get()

        // Then
        repository isNot null
    }

    @Test
    fun `Given resolvePageModule is called it creates a Module, which contains a plain PageService`() {
        // When
        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolvePageModule(),
                module {
                    single<PageContract.Repository> { PageRepositoryStub() }
                    single<MwClientContract.ServiceResponseWrapper> { ServiceResponseWrapperStub() }
                }
            )
        }

        val service: PublicApi.PageService = koin.koin.get()

        // Then
        service isNot null
    }
}
