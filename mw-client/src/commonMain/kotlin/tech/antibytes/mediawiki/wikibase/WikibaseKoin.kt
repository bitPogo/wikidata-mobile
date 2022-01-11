/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase

import kotlinx.serialization.KSerializer
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import tech.antibytes.mediawiki.DataModelContract.BoxedTerms
import tech.antibytes.mediawiki.DataModelContract.LanguageValuePair
import tech.antibytes.mediawiki.annotation.InternalKoinModuleScope
import tech.antibytes.mediawiki.wikibase.model.BoxedTermsSerializer
import tech.antibytes.mediawiki.wikibase.model.LanguageValuePairSerializer

internal fun resolveWikibaseModule(): Module {
    return module {
        @InternalKoinModuleScope
        factory<KSerializer<LanguageValuePair>>(named(WikibaseContract.KoinKey.LANGUAGE_PAIR_SERIALIZER)) {
            LanguageValuePairSerializer()
        }

        @InternalKoinModuleScope
        factory<KSerializer<BoxedTerms>>(named(WikibaseContract.KoinKey.BOXED_TERMS_SERIALIZER)) {
            BoxedTermsSerializer(
                get(named(WikibaseContract.KoinKey.LANGUAGE_PAIR_SERIALIZER))
            )
        }

        @InternalKoinModuleScope
        factory<WikibaseContract.ApiService> {
            WikibaseApiService(get())
        }

        @InternalKoinModuleScope
        factory<WikibaseContract.Repository> {
            WikibaseRepository(
                get(),
                get(),
                get(named(WikibaseContract.KoinKey.BOXED_TERMS_SERIALIZER))
            )
        }

        single<WikibaseContract.Service> {
            WikibaseService(get(), get(), get())
        }
    }
}
