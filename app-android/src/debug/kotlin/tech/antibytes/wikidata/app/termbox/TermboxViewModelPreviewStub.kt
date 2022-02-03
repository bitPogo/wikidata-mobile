/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termbox

import android.graphics.Bitmap
import kotlinx.coroutines.flow.StateFlow
import tech.antibytes.wikidata.app.util.UtilContract

class TermboxViewModelPreviewStub(
    override val id: StateFlow<String>,
    override val isEditable: StateFlow<Boolean>,
    override val label: StateFlow<String>,
    override val description: StateFlow<String>,
    override val aliases: StateFlow<List<String>>,
    override val language: StateFlow<UtilContract.MwLocale>,
    override val qrCode: StateFlow<Bitmap?>
) : TermboxContract.TermboxViewModel {
    override fun setLabel(newLabel: String) = Unit

    override fun setDescription(newDescription: String) = Unit

    override fun addAlias(newAlias: String) = Unit

    override fun setAlias(idx: Int, newAlias: String) = Unit

    override fun dischargeChanges() = Unit

    override fun saveChanges() = Unit

    override fun refresh() = Unit

    override fun createNewItem() = Unit

    override fun randomItem() = Unit

    override fun fetchItem(id: String) = Unit
}
