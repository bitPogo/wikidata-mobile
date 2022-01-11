/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.token

import org.koin.dsl.koinApplication
import org.koin.dsl.module
import tech.antibytes.mediawiki.networking.NetworkingContract
import tech.antibytes.mock.core.token.MetaTokenApiServiceStub
import tech.antibytes.mock.networking.RequestBuilderFactoryStub
import tech.antibytes.util.test.isNot
import kotlin.test.Test

class MetaTokenKoinSpec {
    @Test
    fun `Given resolveMetaTokenModule is called it creates a Module, which contains a plain MetaTokenApiService`() {
        // When
        val koin = koinApplication {
            modules(
                resolveMetaTokenModule(),
                module {
                    single<NetworkingContract.RequestBuilderFactory> { RequestBuilderFactoryStub() }
                }
            )
        }

        val apiService: MetaTokenContract.ApiService = koin.koin.get()

        // Then
        apiService isNot null
    }

    @Test
    fun `Given resolveMetaTokenModule is called it creates a Module, which contains a plain MetaTokenRepository`() {
        // When
        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolveMetaTokenModule(),
                module {
                    single<MetaTokenContract.ApiService> { MetaTokenApiServiceStub() }
                }
            )
        }

        val repository: MetaTokenContract.Repository = koin.koin.get()

        // Then
        repository isNot null
    }
}
