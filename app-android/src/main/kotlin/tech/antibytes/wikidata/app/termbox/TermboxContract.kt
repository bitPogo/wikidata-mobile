/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termbox

import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

interface TermboxContract {
    interface TermboxViewModel {
        val id: StateFlow<String>
        val isEditable: StateFlow<Boolean>
        val label: StateFlow<String>
        val description: StateFlow<String>
        val aliases: StateFlow<List<String>>
        val language: StateFlow<Locale>

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
        }
    }

    interface Navigator {
        fun goToLanguageSelector()
        fun goToTermSearch()
    }
}
