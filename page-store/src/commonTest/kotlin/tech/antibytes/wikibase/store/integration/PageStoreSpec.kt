/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.integration

import kotlinx.coroutines.channels.Channel
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kfixture.listFixture
import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.util.coroutine.result.ResultContract
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract
import tech.antibytes.util.test.annotations.RobolectricConfig
import tech.antibytes.util.test.annotations.RobolectricTestRunner
import tech.antibytes.util.test.annotations.RunWithRobolectricTestRunner
import tech.antibytes.util.test.coroutine.runBlockingTestWithTimeout
import tech.antibytes.util.test.mustBe
import tech.antibytes.wikibase.store.database.page.WikibaseDataBase
import tech.antibytes.wikibase.store.mock.SuspendingFunctionWrapperStub
import tech.antibytes.wikibase.store.mock.client.EntityStub
import tech.antibytes.wikibase.store.mock.client.LanguageValuePairStub
import tech.antibytes.wikibase.store.mock.client.MwClientStub
import tech.antibytes.wikibase.store.mock.client.PagePointerStub
import tech.antibytes.wikibase.store.page.PageStoreContract
import tech.antibytes.wikibase.store.page.database.DatabaseDriver
import tech.antibytes.wikibase.store.page.domain.PageStore
import tech.antibytes.wikibase.store.page.domain.model.EntityId
import tech.antibytes.wikibase.store.page.domain.model.PageModelContract
import tech.antibytes.wikibase.store.page.testScope1
import tech.antibytes.wikibase.store.page.testScope2
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@RobolectricConfig(manifest = "--none")
@RunWithRobolectricTestRunner(RobolectricTestRunner::class)
class PageStoreSpec {
    private val fixture = kotlinFixture()
    private val db = DatabaseDriver()
    private val client = MwClientStub()

    private lateinit var pageStore: PageStoreContract.PageStore

    @BeforeTest
    fun setUp() {
        db.open(WikibaseDataBase.Schema)

        pageStore = PageStore.getInstance(
            client,
            db.dataBase.pageQueries,
            { testScope1 },
            { testScope2 }
        )
    }

    @AfterTest
    fun tearDown() {
        db.close()
    }

    @Test
    fun `Given fetchRandomItem is called it emits RandomItemIds`() {
        // Given
        val actual = Channel<ResultContract<EntityId, Exception>>()

        val remoteIds = fixture.listFixture<String>(size = 5).map { id ->
            PagePointerStub(fixture.fixture(), "Q$id")
        }

        val calls = mutableListOf(
            SuspendingFunctionWrapperStub { remoteIds }
        )

        client.page.randomPage = { _, _ ->
            calls.removeFirst() as CoroutineWrapperContract.SuspendingFunctionWrapper<List<DataModelContract.RevisionedPagePointer>>
        }

        pageStore.randomItemId.subscribeWithSuspendingFunction { id ->
            actual.send(id)
        }

        // When
        pageStore.fetchRandomItem()

        // Then
        runBlockingTestWithTimeout {
            actual.receive().unwrap() mustBe remoteIds.first().title
        }

        // When
        pageStore.fetchRandomItem()

        // Then
        runBlockingTestWithTimeout {
            actual.receive().unwrap() mustBe remoteIds[1].title
        }
    }

    @Test
    fun `Given searchEntities is called it emits SearchEntries`() {
        // Given
        val actual = Channel<ResultContract<List<PageModelContract.SearchEntry>, Exception>>()

        val term: String = fixture.fixture()
        val language: String = fixture.fixture()

        val entries = listOf(
            EntityStub(
                id = fixture.fixture(),
                type = DataModelContract.EntityType.ITEM,
                labels = mapOf(
                    language to LanguageValuePairStub(language, fixture.fixture())
                ),
                descriptions = emptyMap(),
                aliases = emptyMap()
            ),
            EntityStub(
                id = fixture.fixture(),
                type = DataModelContract.EntityType.ITEM,
                labels = emptyMap(),
                descriptions = mapOf(
                    language to LanguageValuePairStub(language, fixture.fixture())
                ),
                aliases = emptyMap()
            )
        )

        val calls = mutableListOf(
            SuspendingFunctionWrapperStub { entries }
        )

        client.wikibase.searchForEntities = { _, _, _, _, _ ->
            calls.removeFirst() as CoroutineWrapperContract.SuspendingFunctionWrapper<List<DataModelContract.Entity>>
        }

        pageStore.searchEntries.subscribeWithSuspendingFunction { entry ->
            actual.send(entry)
        }

        // When
        pageStore.searchItems(term, language)

        // Then
        runBlockingTestWithTimeout {
            val search = actual.receive().unwrap()

            search.first().id mustBe entries.first().id
            search[1].id mustBe entries[1].id
        }
    }
}
