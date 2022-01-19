/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.e2e

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.Instant
import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.mediawiki.MwClient
import tech.antibytes.mediawiki.e2e.test.config.TestConfig
import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.mustBe
import kotlin.test.Ignore
import kotlin.test.Test

@Ignore
class MwClientE2ESpec {
    private val fixture = kotlinFixture()

    fun DataModelContract.RevisionedEntity.toTestEntity(): TestEntity {
        return TestEntity(
            id = this.id,
            type = this.type,
            revision = this.revision,
            lastModification = this.lastModification,
            labels = this.labels,
            descriptions = this.descriptions,
            aliases = this.aliases
        )
    }

    @Test
    fun `It creates a new Entity, fetches it, gets its retrictions and edits it again`() = runBlockingTest {
        val host = "test.wikidata.org"
        val language = "de"
        val entity = TestEntity(
            id = fixture.fixture(),
            type = DataModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            lastModification = Instant.DISTANT_FUTURE,
            labels = mapOf(
                language to LanguageValuePair(
                    language = language,
                    value = fixture.fixture()
                )
            ),
            descriptions = mapOf(
                language to LanguageValuePair(
                    language = language,
                    value = fixture.fixture()
                )
            ),
            aliases = mapOf(
                language to listOf(
                    LanguageValuePair(
                        language = language,
                        value = fixture.fixture()
                    )
                )
            )
        )

        val client = MwClient.getInstance(
            host,
            LoggerStub(),
            { true },
            { CoroutineScope(Dispatchers.Default) }
        )

        val newEntity = client.wikibase.createEntity(DataModelContract.EntityType.ITEM, entity).wrappedFunction.invoke()

        val fetchedEntities = client.wikibase.fetchEntities(setOf(newEntity!!.id)).wrappedFunction.invoke()

        newEntity.labels mustBe fetchedEntities.first().labels
        newEntity.descriptions mustBe fetchedEntities.first().descriptions
        newEntity.aliases mustBe fetchedEntities.first().aliases

        val restrictions = client.page.fetchRestrictions(newEntity.id).wrappedFunction.invoke()

        restrictions mustBe emptyList()

        val editableEntity = newEntity.toTestEntity()
        val modifiedEntity = editableEntity.copy(
            labels = newEntity.labels.toMutableMap().also {
                it["en"] = LanguageValuePair("en", fixture.fixture())
            }
        )

        val response = client.wikibase.updateEntity(modifiedEntity).wrappedFunction.invoke()
        modifiedEntity.labels[language] mustBe response!!.labels[language]
        modifiedEntity.labels["en"]!!.value mustBe response.labels["en"]!!.value
        modifiedEntity.descriptions mustBe response.descriptions
        modifiedEntity.aliases mustBe response.aliases

        val fetchedModifiedEntities = client.wikibase.fetchEntities(
            setOf(modifiedEntity.id),
            "en"
        ).wrappedFunction.invoke().first()

        fetchedModifiedEntities.labels.containsKey(language) mustBe false
        response.labels["en"] mustBe fetchedModifiedEntities.labels["en"]
        response.descriptions["en"] mustBe fetchedModifiedEntities.descriptions["en"]
        response.aliases["en"] mustBe fetchedModifiedEntities.aliases["en"]
    }

    @Test
    fun `It logins in and fetches a random Entity`() = runBlockingTest {
        val host = "test.wikidata.org"

        val client = MwClient.getInstance(
            host,
            LoggerStub(),
            { true },
            { CoroutineScope(Dispatchers.Default) }
        )

        client.authentication.login("nop", "nop").wrappedFunction.invoke() mustBe false
        client.authentication.login(TestConfig.username, TestConfig.password).wrappedFunction.invoke() mustBe true

        val pages = client.page.randomPage(5).wrappedFunction.invoke()
        val titlePrefix = pages.first().title.startsWith("P") || pages.first().title.startsWith("Q")
        titlePrefix mustBe true
    }

    @Test
    fun `It creates and finds a Entity`() = runBlockingTest {
        val host = "test.wikidata.org"
        val language = "de"
        val entity = TestEntity(
            id = fixture.fixture(),
            type = DataModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            lastModification = Instant.DISTANT_FUTURE,
            labels = mapOf(
                language to LanguageValuePair(
                    language = language,
                    value = fixture.fixture()
                )
            ),
            descriptions = mapOf(
                language to LanguageValuePair(
                    language = language,
                    value = fixture.fixture()
                )
            ),
            aliases = mapOf(
                language to listOf(
                    LanguageValuePair(
                        language = language,
                        value = fixture.fixture()
                    )
                )
            )
        )

        val client = MwClient.getInstance(
            host,
            LoggerStub(),
            { true },
            { CoroutineScope(Dispatchers.Default) }
        )

        val newEntity = client.wikibase.createEntity(DataModelContract.EntityType.ITEM, entity).wrappedFunction.invoke()
        client.wikibase.searchForEntities(
            newEntity!!.labels[language]!!.value,
            language,
            newEntity.type,
            10
        ).wrappedFunction.invoke()
    }
}
