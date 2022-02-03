/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.di

import android.content.Context
import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tech.antibytes.mediawiki.MwClient
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.CoroutineScopeDispatcher
import tech.antibytes.wikibase.store.database.WikibaseDataBase
import tech.antibytes.wikibase.store.entity.EntityStoreContract
import tech.antibytes.wikibase.store.entity.domain.EntityStore
import tech.antibytes.wikibase.store.page.PageStoreContract
import tech.antibytes.wikibase.store.page.domain.PageStore
import tech.antibytes.wikibase.store.user.UserStoreContract
import tech.antibytes.wikibase.store.user.domain.UserStore
import tech.antibytes.wikidata.app.ApplicationContract
import tech.antibytes.wikidata.app.BuildConfig
import tech.antibytes.wikidata.app.util.ConnectivityManager
import tech.antibytes.wikidata.app.util.DatabaseFactory
import tech.antibytes.wikidata.app.util.MwLocale
import tech.antibytes.wikidata.app.util.SupportedWikibaseLanguages
import tech.antibytes.wikidata.app.util.UtilContract
import java.util.Locale
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class SupportedLanguages
@Qualifier
annotation class MutableLanguageHandle
@Qualifier
annotation class LanguageState
@Qualifier
annotation class IODispatcher
@Qualifier
annotation class FlowDispatcher

@Module
@InstallIn(SingletonComponent::class)
object DependencyProvider {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): WikibaseDataBase {
        return DatabaseFactory.create(
            WikibaseDataBase.Schema,
            context
        )
    }

    @Singleton
    @Provides
    fun provideConnectivityManager(
        @ApplicationContext context: Context
    ): PublicApi.ConnectivityManager = ConnectivityManager(context)

    @Singleton
    @Provides
    @SupportedLanguages
    fun provideWikibaseLanguages(): List<UtilContract.MwLocale> = SupportedWikibaseLanguages.get()

    @Singleton
    @Provides
    fun provideLogger(): PublicApi.Logger {
        return object : PublicApi.Logger {
            override fun info(message: String) {
                Log.d(
                    ApplicationContract.LogTag.CLIENT_INFO.value,
                    message
                )
            }

            override fun warn(message: String) {
                Log.d(
                    ApplicationContract.LogTag.CLIENT_WARN.value,
                    message
                )
            }

            override fun error(exception: Throwable, message: String?) {
                Log.d(
                    ApplicationContract.LogTag.CLIENT_ERROR.value,
                    message ?: exception.toString()
                )
            }

            override fun log(message: String) {
                Log.d(
                    ApplicationContract.LogTag.CLIENT_LOG.value,
                    message
                )
            }
        }
    }

    @Singleton
    @Provides
    @IODispatcher
    fun provideIODispatcher(): CoroutineScopeDispatcher {
        return CoroutineScopeDispatcher { CoroutineScope(Dispatchers.IO) }
    }

    @Singleton
    @Provides
    @FlowDispatcher
    fun provideStoreDispatcher(): CoroutineScopeDispatcher {
        return CoroutineScopeDispatcher { CoroutineScope(Dispatchers.Default) }
    }

    @Singleton
    @Provides
    fun provideClient(
        logger: PublicApi.Logger,
        connectivityManager: PublicApi.ConnectivityManager,
        @IODispatcher dispatcher: CoroutineScopeDispatcher
    ): PublicApi.Client {
        return MwClient.getInstance(
            host = BuildConfig.ENDPOINT,
            logger = logger,
            connection = connectivityManager,
            dispatcher = dispatcher
        )
    }

    @Singleton
    @Provides
    fun provideEntityStore(
        client: PublicApi.Client,
        dataBase: WikibaseDataBase,
        @IODispatcher ioDispatcher: CoroutineScopeDispatcher,
        @FlowDispatcher flowDispatcher: CoroutineScopeDispatcher
    ): EntityStoreContract.EntityStore {
        return EntityStore.getInstance(
            client = client,
            database = dataBase.entityQueries,
            producerScope = ioDispatcher,
            consumerScope = flowDispatcher,
        )
    }

    @Singleton
    @Provides
    fun providePageStore(
        client: PublicApi.Client,
        dataBase: WikibaseDataBase,
        @IODispatcher ioDispatcher: CoroutineScopeDispatcher,
        @FlowDispatcher flowDispatcher: CoroutineScopeDispatcher
    ): PageStoreContract.PageStore {
        return PageStore.getInstance(
            client = client,
            database = dataBase.pageQueries,
            producerScope = ioDispatcher,
            consumerScope = flowDispatcher,
        )
    }

    @Singleton
    @Provides
    fun provideUserStore(
        client: PublicApi.Client,
        @IODispatcher ioDispatcher: CoroutineScopeDispatcher,
        @FlowDispatcher flowDispatcher: CoroutineScopeDispatcher
    ): UserStoreContract.UserStore {
        return UserStore.getInstance(
            client = client,
            producerScope = ioDispatcher,
            consumerScope = flowDispatcher,
        )
    }

    @Singleton
    @Provides
    @MutableLanguageHandle
    fun provideMutableLanguageHandle(): MutableStateFlow<UtilContract.MwLocale> {
        return MutableStateFlow(
            MwLocale(Locale.getDefault().toLanguageTag()) // TODO
        )
    }

    @Singleton
    @Provides
    @LanguageState
    fun provideLanguageState(
        @MutableLanguageHandle language: MutableStateFlow<UtilContract.MwLocale>
    ): StateFlow<UtilContract.MwLocale> = language
}
