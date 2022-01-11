/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.authentication

import org.koin.dsl.koinApplication
import org.koin.dsl.module
import tech.antibytes.mediawiki.MwClientContract
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.mediawiki.core.token.MetaTokenContract
import tech.antibytes.mediawiki.networking.NetworkingContract
import tech.antibytes.mock.ServiceResponseWrapperStub
import tech.antibytes.mock.core.authentication.AuthenticationApiServiceStub
import tech.antibytes.mock.core.authentication.AuthenticationRepositoryStub
import tech.antibytes.mock.core.token.MetaTokenRepositoryStub
import tech.antibytes.mock.networking.RequestBuilderFactoryStub
import tech.antibytes.util.test.isNot
import kotlin.test.Test

class AuthenticationKoinSpec {
    @Test
    fun `Given resolveAuthenticationModule is called it creates a Module, which contains a plain AuthenticationApiService`() {
        // When
        val koin = koinApplication {
            modules(
                resolveAuthenticationModule(),
                module {
                    single<NetworkingContract.RequestBuilderFactory> { RequestBuilderFactoryStub() }
                }
            )
        }

        val apiService: AuthenticationContract.ApiService = koin.koin.get()

        // Then
        apiService isNot null
    }

    @Test
    fun `Given resolveAuthenticationModule is called it creates a Module, which contains a plain AuthenticationRepository`() {
        // When
        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolveAuthenticationModule(),
                module {
                    single<AuthenticationContract.ApiService> { AuthenticationApiServiceStub() }
                }
            )
        }

        val repository: AuthenticationContract.Repository = koin.koin.get()

        // Then
        repository isNot null
    }

    @Test
    fun `Given resolveAuthenticationModule is called it creates a Module, which contains a plain AuthenticationService`() {
        // When
        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolveAuthenticationModule(),
                module {
                    single<AuthenticationContract.Repository> { AuthenticationRepositoryStub() }
                    single<MwClientContract.ServiceResponseWrapper> { ServiceResponseWrapperStub() }
                    single<MetaTokenContract.Repository> { MetaTokenRepositoryStub() }
                }
            )
        }

        val service: PublicApi.AuthenticationService = koin.koin.get()

        // Then
        service isNot null
    }
}
