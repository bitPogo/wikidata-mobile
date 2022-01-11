/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase

import kotlinx.datetime.Clock
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.mediawiki.MwClientContract
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.mediawiki.core.token.MetaTokenContract
import tech.antibytes.mediawiki.networking.NetworkingContract
import tech.antibytes.mock.ClockStub
import tech.antibytes.mock.ServiceResponseWrapperStub
import tech.antibytes.mock.core.token.MetaTokenRepositoryStub
import tech.antibytes.mock.networking.RequestBuilderFactoryStub
import tech.antibytes.mock.serialization.SerializerStub
import tech.antibytes.mock.wikibase.WikibaseApiServiceStub
import tech.antibytes.mock.wikibase.WikibaseRepositoryStub
import tech.antibytes.util.test.isNot
import kotlin.test.Test

class WikibaseKoinSpec {
    @Test
    fun `Given resolveWikibaseModule is called it creates a Module, which contains a plain LanguagePairSerializer`() {
        // When
        val koin = koinApplication {
            modules(resolveWikibaseModule())
        }

        val serializer: KSerializer<DataModelContract.LanguageValuePair> = koin.koin.get(
            named(WikibaseContract.KoinKey.LANGUAGE_PAIR_SERIALIZER)
        )

        // Then
        serializer isNot null
    }

    @Test
    fun `Given resolveWikibaseModule is called it creates a Module, which contains a plain BoxedTermsSerializer`() {
        // When
        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolveWikibaseModule(),
                module {
                    single<KSerializer<DataModelContract.LanguageValuePair>>(named(WikibaseContract.KoinKey.LANGUAGE_PAIR_SERIALIZER)) {
                        SerializerStub(
                            valueDescriptor = PrimitiveSerialDescriptor("test", PrimitiveKind.STRING)
                        )
                    }
                }
            )
        }

        val serializer: KSerializer<DataModelContract.BoxedTerms> = koin.koin.get(
            named(WikibaseContract.KoinKey.BOXED_TERMS_SERIALIZER)
        )

        // Then
        serializer isNot null
    }

    @Test
    fun `Given resolveWikibaseModule is called it creates a Module, which contains a plain WikibaseApiService`() {
        // When
        val koin = koinApplication {
            modules(
                resolveWikibaseModule(),
                module {
                    single<NetworkingContract.RequestBuilderFactory> { RequestBuilderFactoryStub() }
                }
            )
        }

        val apiService: WikibaseContract.ApiService = koin.koin.get()

        // Then
        apiService isNot null
    }

    @Test
    fun `Given resolveWikibaseModule is called it creates a Module, which contains a plain WikibaseRepository`() {
        // When
        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolveWikibaseModule(),
                module {
                    single<WikibaseContract.ApiService> { WikibaseApiServiceStub() }
                    single<Json> { Json }
                    single<Clock> { ClockStub() }
                    single<KSerializer<DataModelContract.BoxedTerms>>(named(WikibaseContract.KoinKey.BOXED_TERMS_SERIALIZER)) {
                        SerializerStub()
                    }
                }
            )
        }

        val repository: WikibaseContract.Repository = koin.koin.get()

        // Then
        repository isNot null
    }

    @Test
    fun `Given resolveWikibaseModule is called it creates a Module, which contains a plain WikibaseService`() {
        // When
        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolveWikibaseModule(),
                module {
                    single<WikibaseContract.Repository> { WikibaseRepositoryStub() }
                    single<MetaTokenContract.Repository> { MetaTokenRepositoryStub() }
                    single<MwClientContract.ServiceResponseWrapper> { ServiceResponseWrapperStub() }
                }
            )
        }

        val service: PublicApi.WikibaseService = koin.koin.get()

        // Then
        service isNot null
    }
}
