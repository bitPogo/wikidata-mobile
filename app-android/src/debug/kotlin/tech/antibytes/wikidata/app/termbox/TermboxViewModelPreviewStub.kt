/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termbox

import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

class TermboxViewModelPreviewStub(
    override val id: StateFlow<String>,
    override val isEditable: StateFlow<Boolean>,
    override val label: StateFlow<String>,
    override val description: StateFlow<String>,
    override val aliases: StateFlow<List<String>>,
    override val language: StateFlow<Locale>
) : TermboxContract.TermboxViewModel {
    override fun setLabel(newLabel: String) {
    }

    override fun setDescription(newDescription: String) {
    }

    override fun addAlias(newAlias: String) {
    }

    override fun setAlias(idx: Int, newAlias: String) {
    }

    override fun dischargeChanges() {
    }

    override fun saveChanges() {
    }

    override fun refresh() {
    }

    override fun createNewItem() {
    }

    override fun randomItem() {
    }

    override fun fetchItem(id: String) {
    }
}
