/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.mock

import kotlinx.coroutines.flow.StateFlow
import tech.antibytes.wikidata.app.termbox.TermboxContract
import java.util.Locale

class TermboxViewModelAndroidStub(
    override val id: StateFlow<String>,
    override val isEditable: StateFlow<Boolean>,
    override val label: StateFlow<String>,
    override val description: StateFlow<String>,
    override val aliases: StateFlow<List<String>>,
    override val language: StateFlow<Locale>,
    var setLabel: ((String) -> Unit)? = null,
    var setDescription: ((String) -> Unit)? = null,
    var addAlias: ((String) -> Unit)? = null,
    var setAlias: ((Int, String) -> Unit)? = null,
    var dischargeChanges: (() -> Unit)? = null,
    var saveChanges: (() -> Unit)? = null,
    var refresh: (() -> Unit)? = null,
    var createNewItem: (() -> Unit)? = null,
    var randomItem: (() -> Unit)? = null,
    var fetchItem: ((String) -> Unit)? = null
) : TermboxContract.TermboxViewModel {
    override fun setLabel(newLabel: String) {
        return setLabel?.invoke(newLabel)
            ?: throw RuntimeException("Missing Sideeffect setLabel")
    }

    override fun setDescription(newDescription: String) {
        return setDescription?.invoke(newDescription)
            ?: throw RuntimeException("Missing Sideeffect setDescription")
    }

    override fun addAlias(newAlias: String) {
        return addAlias?.invoke(newAlias)
            ?: throw RuntimeException("Missing Sideeffect addAlias")
    }

    override fun setAlias(idx: Int, newAlias: String) {
        return setAlias?.invoke(idx, newAlias)
            ?: throw RuntimeException("Missing Sideeffect setAlias")
    }

    override fun dischargeChanges() {
        return dischargeChanges?.invoke()
            ?: throw RuntimeException("Missing Sideeffect dischargeChanges")
    }

    override fun saveChanges() {
        return saveChanges?.invoke()
            ?: throw RuntimeException("Missing Sideeffect saveChanges")
    }

    override fun refresh() {
        return refresh?.invoke()
            ?: throw RuntimeException("Missing Sideeffect refresh")
    }

    override fun createNewItem() {
        return createNewItem?.invoke()
            ?: throw RuntimeException("Missing Sideeffect createNewItem")
    }

    override fun randomItem() {
        return randomItem?.invoke()
            ?: throw RuntimeException("Missing Sideeffect randomItem")
    }

    override fun fetchItem(id: String) {
        return fetchItem?.invoke(id)
            ?: throw RuntimeException("Missing Sideeffect fetchItem")
    }

    fun clear() {
        setLabel = null
        setDescription = null
        addAlias = null
        setAlias = null
        dischargeChanges = null
        saveChanges = null
        refresh = null
        createNewItem = null
        randomItem = null
        fetchItem = null
    }
}
