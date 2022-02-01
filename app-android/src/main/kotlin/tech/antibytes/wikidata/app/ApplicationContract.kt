/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app

interface ApplicationContract {
    enum class LogTag(val value: String) {
        TERMBOX_VIEWMODEL("TERMBOX_VIEWMODEL"),
        LANGUAGE_SELECTOR_VIEWMODEL("LANGUAGE_SELECTOR_VIEWMODEL"),
        TERMSEARCH_VIEWMODEL("TERMSEARCH_VIEWMODEL"),
        CLIENT_INFO("MWCLIENT_INFO"),
        CLIENT_WARN("MWCLIENT_WARN"),
        CLIENT_ERROR("MWCLIENT_ERROR"),
        CLIENT_LOG("MWCLIENT_VERBOSE")
    }
}
