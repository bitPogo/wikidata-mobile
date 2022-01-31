/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app

interface ApplicationContract {
    enum class LogTag(val value: String) {
        TERMBOX_VIEWMODEL("termbox_vm")
    }
}
