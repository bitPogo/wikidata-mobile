/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.di

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.After
import org.junit.Test
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.mediawiki.MwClient
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.CoroutineScopeDispatcher
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs
import tech.antibytes.wikibase.store.database.WikibaseDataBase
import tech.antibytes.wikibase.store.database.entity.EntityQueries
import tech.antibytes.wikibase.store.database.page.PageQueries
import tech.antibytes.wikibase.store.entity.EntityStoreContract
import tech.antibytes.wikibase.store.entity.domain.EntityStore
import tech.antibytes.wikibase.store.page.PageStoreContract
import tech.antibytes.wikibase.store.page.domain.PageStore
import tech.antibytes.wikibase.store.user.UserStoreContract
import tech.antibytes.wikibase.store.user.domain.UserStore
import tech.antibytes.wikidata.app.BuildConfig
import tech.antibytes.wikidata.app.util.DatabaseFactory
import tech.antibytes.wikidata.app.util.SupportedWikibaseLanguages
import tech.antibytes.wikidata.app.util.UtilContract
import tech.antibytes.wikidata.mock.MwLocaleStub
import java.util.Locale

class DependencyProviderSpec {
    private val fixture = kotlinFixture()

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `Given provideDatabase is called with a context, it delegates the call to the DatabaseFactory`() {
        // Given
        mockkObject(DatabaseFactory)
        val context: Context = mockk(relaxed = true)

        val dataBase: WikibaseDataBase = mockk()

        every {
            DatabaseFactory.create(
                WikibaseDataBase.Schema,
                context
            )
        } returns dataBase

        // When
        val actual = DependencyProvider.provideDatabase(context)

        // Then
        actual sameAs dataBase
    }

    @Test
    fun `Given provideConnectivityManager is called with a context is creates a ConnectivityManager`() {
        // Given
        val context: Context = mockk(relaxed = true)

        // When
        val actual = DependencyProvider.provideConnectivityManager(context)

        // Then
        actual fulfils PublicApi.ConnectivityManager::class
    }

    @Test
    fun `Given provideWikibaseLanguages is called, it delegates the call to SupportedWikibaseLanguages`() {
        // Given
        mockkObject(SupportedWikibaseLanguages)

        val languages: List<UtilContract.MwLocale> = mockk()

        every { SupportedWikibaseLanguages.get() } returns languages

        // When
        val actual = DependencyProvider.provideWikibaseLanguages()

        // Then
        actual sameAs languages
    }

    @Test
    fun `Given provideLogger is called it creates a Logger`() {
        // When
        val actual = DependencyProvider.provideLogger()

        // Then
        actual fulfils PublicApi.Logger::class
    }

    @Test
    fun `Given provideClient is called with a Logger and ConnectivityManager, it creates a Client`() {
        mockkObject(MwClient)

        val logger: PublicApi.Logger = mockk()
        val connectivityManager: PublicApi.ConnectivityManager = mockk()

        val client: PublicApi.Client = mockk()
        val dispatcher = CoroutineScopeDispatcher { CoroutineScope(Dispatchers.Default) }

        every {
            MwClient.getInstance(
                BuildConfig.ENDPOINT,
                logger,
                connectivityManager,
                dispatcher
            )
        } returns client

        // When
        val actual = DependencyProvider.provideClient(logger, connectivityManager, dispatcher)

        // Then
        actual sameAs client
    }

    @Test
    fun `Given provideEntityStore is called with a Client and DataBase, it creates a EntityStore`() {
        mockkObject(EntityStore)

        val client: PublicApi.Client = mockk()
        val database: WikibaseDataBase = mockk()
        val entityQueries: EntityQueries = mockk()
        val ioDispatcher = CoroutineScopeDispatcher { CoroutineScope(Dispatchers.Default) }
        val flowDispatcher = CoroutineScopeDispatcher { CoroutineScope(Dispatchers.Main) }

        val entityStore: EntityStoreContract.EntityStore = mockk()

        every { database.entityQueries } returns entityQueries

        every {
            EntityStore.getInstance(
                client,
                entityQueries,
                ioDispatcher,
                flowDispatcher
            )
        } returns entityStore

        // When
        val actual = DependencyProvider.provideEntityStore(client, database, ioDispatcher, flowDispatcher)

        // Then
        actual sameAs entityStore
    }

    @Test
    fun `Given providePageStore is called with a Client and DataBase, it creates a PageStore`() {
        mockkObject(PageStore)

        val client: PublicApi.Client = mockk()
        val database: WikibaseDataBase = mockk()
        val pageQueries: PageQueries = mockk()
        val ioDispatcher = CoroutineScopeDispatcher { CoroutineScope(Dispatchers.Default) }
        val flowDispatcher = CoroutineScopeDispatcher { CoroutineScope(Dispatchers.Main) }

        val pageStore: PageStoreContract.PageStore = mockk()

        every { database.pageQueries } returns pageQueries

        every {
            PageStore.getInstance(
                client,
                pageQueries,
                ioDispatcher,
                flowDispatcher
            )
        } returns pageStore

        // When
        val actual = DependencyProvider.providePageStore(client, database, ioDispatcher, flowDispatcher)

        // Then
        actual sameAs pageStore
    }

    @Test
    fun `Given provideUserStore is called with a Client, it creates a UserStore`() {
        mockkObject(UserStore)

        val client: PublicApi.Client = mockk()
        val ioDispatcher = CoroutineScopeDispatcher { CoroutineScope(Dispatchers.Default) }
        val flowDispatcher = CoroutineScopeDispatcher { CoroutineScope(Dispatchers.Main) }

        val userStore: UserStoreContract.UserStore = mockk()

        every {
            UserStore.getInstance(
                client,
                ioDispatcher,
                flowDispatcher
            )
        } returns userStore

        // When
        val actual = DependencyProvider.provideUserStore(client, ioDispatcher, flowDispatcher)

        // Then
        actual sameAs userStore
    }

    @Test
    fun `Given provideMutableLanguageHandle is called, it provides a MutableStateFlow, with the default Locale`() {
        // When
        val actual = DependencyProvider.provideMutableLanguageHandle()

        // Then
        actual fulfils MutableStateFlow::class
        actual.value.asLocale() mustBe Locale.getDefault()
    }

    @Test
    fun `Given provideLanguageState is called with a StateFlow, it reflects the given flow`() {
        // Given
        val flow = MutableStateFlow<UtilContract.MwLocale>(
            MwLocaleStub(fixture.fixture(), fixture.fixture(), fixture.fixture())
        )

        // When
        val actual = DependencyProvider.provideLanguageState(flow)

        // Then
        actual sameAs flow
    }

    @Test
    fun `Given provideIODispatcher is called, it returns a CoroutineScopeDispatcher`() {
        // When
        val dispatcher = DependencyProvider.provideIODispatcher()

        // Then
        dispatcher.dispatch()
            .toString()
            .contains("Dispatchers.IO") mustBe true
    }

    @Test
    fun `Given provideStoreDispatcher is called, it returns a CoroutineScopeDispatcher`() {
        // When
        val dispatcher = DependencyProvider.provideStoreDispatcher()

        // Then
        dispatcher.dispatch()
            .toString()
            .contains("Dispatchers.Default") mustBe true
    }
}
