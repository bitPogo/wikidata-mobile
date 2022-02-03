/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termbox

import android.graphics.Bitmap
import kotlinx.coroutines.flow.StateFlow
import tech.antibytes.wikidata.app.BuildConfig
import tech.antibytes.wikidata.app.util.UtilContract

interface TermboxContract {
    interface TermboxViewModel {
        val id: StateFlow<String>
        val isEditable: StateFlow<Boolean>
        val label: StateFlow<String>
        val description: StateFlow<String>
        val aliases: StateFlow<List<String>>
        val language: StateFlow<UtilContract.MwLocale>
        val qrCode: StateFlow<Bitmap?>

        fun setLabel(newLabel: String)
        fun setDescription(newDescription: String)
        fun setAlias(idx: Int, newAlias: String)
        fun addAlias(newAlias: String)

        fun dischargeChanges()
        fun saveChanges()

        fun refresh()
        fun createNewItem()
        fun randomItem()
        fun fetchItem(id: String)

        companion object {
            const val INITIAL_ENTITY = "Q214750"
            const val QRCODE_TEMPLATE = "https://${BuildConfig.ENDPOINT}/wiki/"
        }
    }

    interface Navigator {
        fun goToLanguageSelector()
        fun goToTermSearch()
    }
}
