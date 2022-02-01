/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termbox

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import tech.antibytes.wikibase.store.entity.EntityStoreContract
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract
import tech.antibytes.wikibase.store.entity.domain.model.LanguageTag
import tech.antibytes.wikibase.store.page.PageStoreContract
import tech.antibytes.wikidata.app.ApplicationContract
import tech.antibytes.wikidata.app.termbox.TermboxContract.TermboxViewModel.Companion.INITIAL_ENTITY
import java.util.Locale
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class TermboxViewModel @Inject constructor(
    @Named("LanguageHandle") override val language: StateFlow<Locale>,
    private val entityStore: EntityStoreContract.EntityStore,
    private val pageStore: PageStoreContract.PageStore,
) : TermboxContract.TermboxViewModel, ViewModel() {
    private val _id = MutableStateFlow("")
    override val id: StateFlow<String> = _id

    private val edibility = MutableStateFlow(false)
    override val isEditable: StateFlow<Boolean> = edibility

    private val _label = MutableStateFlow("")
    override val label: StateFlow<String> = _label

    private val _description = MutableStateFlow("")
    override val description: StateFlow<String> = _description

    private val _aliases: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    override val aliases: StateFlow<List<String>> = _aliases

    init {
        entityStore.entity.subscribeWithSuspendingFunction { entity ->
            if (entity.isSuccess()) {
                distributeEntity(entity.unwrap())
            } else {
                Log.d(
                    ApplicationContract.LogTag.TERMBOX_VIEWMODEL.value,
                    entity.error?.message ?: entity.error.toString()
                )
            }
        }

        fetchItem(INITIAL_ENTITY)
    }

    private fun distributeEntity(entity: EntityModelContract.MonolingualEntity) {
        _id.update { entity.id }
        edibility.update { entity.isEditable }
        _label.update { entity.label ?: "" }
        _description.update { entity.description ?: "" }
        _aliases.update { entity.aliases }
    }

    override fun setLabel(newLabel: String) = entityStore.setLabel(newLabel)

    override fun setDescription(newDescription: String) = entityStore.setDescription(newDescription)

    override fun setAlias(idx: Int, newAlias: String) = entityStore.setAlias(idx, newAlias)

    override fun addAlias(newAlias: String) {
        val aliases = _aliases.value.toMutableList()
        aliases.add(newAlias)

        entityStore.setAliases(aliases)
    }

    override fun dischargeChanges() = entityStore.rollback()

    override fun saveChanges() = entityStore.save()

    override fun refresh() = entityStore.refresh()

    private fun getCurrentLanguageTag(): LanguageTag {
        return language.value.toLanguageTag().replace('_', '-')
    }

    override fun createNewItem() {
        entityStore.create(
            language = getCurrentLanguageTag(),
            type = EntityModelContract.EntityType.ITEM
        )
    }

    override fun randomItem() {
        pageStore.randomItemId.subscribe { result ->
            if (result.isSuccess()) {
                fetchItem(result.unwrap())
            }
        }

        pageStore.fetchRandomItem()
    }

    override fun fetchItem(id: String) {
        entityStore.fetchEntity(
            id = id,
            language = getCurrentLanguageTag(),
        )
    }
}
