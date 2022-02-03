/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termbox

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import tech.antibytes.wikibase.store.entity.EntityStoreContract
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract
import tech.antibytes.wikibase.store.entity.lang.EntityStoreError
import tech.antibytes.wikibase.store.page.PageStoreContract
import tech.antibytes.wikidata.app.ApplicationContract
import tech.antibytes.wikidata.app.di.LanguageState
import tech.antibytes.wikidata.app.termbox.TermboxContract.TermboxViewModel.Companion.INITIAL_ENTITY
import tech.antibytes.wikidata.app.util.UtilContract.MwLocale
import javax.inject.Inject
import tech.antibytes.wikidata.app.termbox.TermboxContract.TermboxViewModel.Companion.QRCODE_TEMPLATE
import tech.antibytes.wikidata.lib.qr.QrCodeStoreContract

@HiltViewModel
class TermboxViewModel @Inject constructor(
    @LanguageState override val language: @JvmSuppressWildcards(true) StateFlow<MwLocale>,
    private val entityStore: EntityStoreContract.EntityStore,
    private val pageStore: PageStoreContract.PageStore,
    private val qrCodeStore: QrCodeStoreContract.QrCodeStore,
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

    private val _qrCode: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    override val qrCode: StateFlow<Bitmap?> = _qrCode

    private fun subscribeOnEntity() {
        entityStore.entity.subscribeWithSuspendingFunction { entity ->
            when {
                entity.isSuccess() -> distributeEntity(entity.unwrap())
                entity.error is EntityStoreError.InitialState -> fetchItem(INITIAL_ENTITY)
                else -> {
                    Log.d(
                        ApplicationContract.LogTag.TERMBOX_VIEWMODEL.value,
                        entity.error?.message ?: entity.error.toString()
                    )
                }
            }
        }
    }

    private fun subscribeOnQrCode() {
        qrCodeStore.qrCode.subscribe { qrCode ->
            if (qrCode.isError()) {
                Log.d(
                    ApplicationContract.LogTag.TERMBOX_VIEWMODEL.value,
                    qrCode.error?.message ?: qrCode.error.toString()
                )

                clearQrCode()
            } else {
                _qrCode.update { qrCode.unwrap() }
            }
        }
    }

    init {
        subscribeOnEntity()
        subscribeOnQrCode()
    }

    private fun fetchQrCode(id: String) = qrCodeStore.fetch(QRCODE_TEMPLATE + id)

    private fun clearQrCode() {
        _qrCode.update { null }
    }

    private fun upDateQrCode(id: String) {
        if (id.isNotEmpty()) {
            fetchQrCode(id)
        } else {
            clearQrCode()
        }
    }

    private fun distributeEntity(entity: EntityModelContract.MonolingualEntity) {
        _id.update { entity.id }
        edibility.update { entity.isEditable }
        _label.update { entity.label ?: "" }
        _description.update { entity.description ?: "" }
        _aliases.update { entity.aliases }

        upDateQrCode(entity.id)
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

    override fun createNewItem() {
        entityStore.create(
            language = language.value.toLanguageTag(),
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
            language = language.value.toLanguageTag(),
        )
    }
}
